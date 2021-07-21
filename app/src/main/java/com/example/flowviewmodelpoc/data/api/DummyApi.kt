package com.example.flowviewmodelpoc.data.api

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class DummyApi(private val retryTimes: Int = DEFAULT_RETRY_COUNT): CoroutineScope {
    override val coroutineContext: CoroutineContext = Job() + Dispatchers.IO
    private var retryCount = 0

    suspend fun fetch(): String {
        delay(DELAY)
        ++retryCount
        Log.d("FETCH_FLOW", "Retry count: $retryCount")
        return if (retryCount == retryTimes) {
            retryCount = 0
            UUID.randomUUID().toString()
        } else {
            ""
        }
    }

    companion object {
        const val DELAY = 1000L
        const val DEFAULT_RETRY_COUNT = 5
    }
}