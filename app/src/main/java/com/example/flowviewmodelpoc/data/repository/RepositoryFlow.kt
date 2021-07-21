package com.example.flowviewmodelpoc.data.repository

import com.example.flowviewmodelpoc.data.api.DummyApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

interface RepositoryFlow {
    fun fetch(): Flow<String>
    fun generateFetchJob(): FetchJob
    suspend fun generateJob(onResult: (result: String) -> Unit): Job
}

class RepositoryFlowImpl: RepositoryFlow {
    private val api = DummyApi(3)

    override fun fetch(): Flow<String> = flow {
            var result = ""
            while (result.isBlank()) {
                result = api.fetch()
                emit(result)
                delay(2000)
            }
        }.onStart {
            emit("")
        }

    override fun generateFetchJob() = FetchJob(api);

    override suspend fun generateJob(onResult: (result: String) -> Unit): Job = coroutineScope() {
        return@coroutineScope launch(context = Dispatchers.IO) {
            var result = ""
            while (result.isBlank()) {
                delay(2000)
                result = api.fetch()
                onResult(result)
            }
        }
    }


}

class FetchJob(private val api: DummyApi): CoroutineScope {
    override val coroutineContext = Job() + Dispatchers.IO

    val isActive get() = coroutineContext.job.isActive
    val isCancelled get() = coroutineContext.job.isCancelled
    val isCompleted get() = _completedFetching || coroutineContext.job.isCompleted
    private var _completedFetching = false

    val fetchFlow = flow {
            var result = ""
            while (result.isBlank()) {
                ensureActive()
                result = api.fetch()
                ensureActive()
                emit(result)
                delay(2000)
            }
            _completedFetching = true
        }.cancellable()
        .onStart {
            emit("")
        }

    fun cancelFetching() {
        cancel("Implicitly cancelled")
    }
}
