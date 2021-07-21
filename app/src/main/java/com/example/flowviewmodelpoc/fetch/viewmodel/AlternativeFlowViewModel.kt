package com.example.flowviewmodelpoc.fetch.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.flowviewmodelpoc.data.repository.FetchJob
import com.example.flowviewmodelpoc.data.repository.RepositoryFlow
import com.example.flowviewmodelpoc.fetch.viewmodel.state.FetchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class AlternativeFlowViewModel(private val repository: RepositoryFlow): ViewModel() {
    private var fetchJob: FetchJob? = null
    private var result: LiveData<FetchState>? = null
    val fetchState get() = result?.value ?: FetchState.NotStarted

    fun start(observer: Observer<FetchState>, owner: LifecycleOwner) {
        if (fetchJob?.isCompleted == true) {
            result?.apply {
                observe(owner, observer)
            }
            return
        }

        result?.removeObserver(observer)

        val resultFlow = run {
            fetchJob?.takeIf { it.isActive }
                ?: repository.generateFetchJob().also { fetchJob = it }
        }.fetchFlow

        result = resultFlow.map { uuid ->
            if (uuid.isBlank()) {
                FetchState.Fetching
            } else {
                FetchState.Completed(uuid = uuid)
            }
        }.catch {
            emit(FetchState.Canceled)
        }
        .flowOn(Dispatchers.IO)
        .asLiveData()
        .apply {
            observe(owner, observer)
        }
    }

    fun forceStop() {
        fetchJob?.cancelFetching()
    }
}