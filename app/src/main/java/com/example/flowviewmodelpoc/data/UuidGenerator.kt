package com.example.flowviewmodelpoc.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class UuidGenerator(val refreshDelay: Long): CoroutineScope {
    override val coroutineContext: CoroutineContext = Job() + Dispatchers.IO

    private var uuid = ""
    val _latestUUID = uuid

    private val uuidChangeCallbacks: MutableList<GeneratorCallback> = mutableListOf()

    private var childJob: Job? = null

    private val _connected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val connected: StateFlow<Boolean> = _connected.asStateFlow()

    fun start() {
        if (childJob?.isCancelled?.not() == true) return

        childJob = launch {
            while (connected.value) {
                uuid = UUID.randomUUID().toString()

                uuidChangeCallbacks.forEach {
                    it.onUuidChange(newUuid = uuid)
                }

                delay(refreshDelay)
            }
        }

        _connected.value = true
    }

    fun stop() {
        coroutineContext.cancelChildren()
        _connected.value = false
    }

    fun addCallback(callback: GeneratorCallback) {
        if (uuidChangeCallbacks.contains(callback)) return

        uuidChangeCallbacks.add(callback)
    }

    fun removeCallback(callback: GeneratorCallback) {
        uuidChangeCallbacks.remove(callback)
    }

    interface GeneratorCallback {
        fun onUuidChange(newUuid: String)
    }
}