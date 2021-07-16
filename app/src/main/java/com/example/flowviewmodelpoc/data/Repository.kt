package com.example.flowviewmodelpoc.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine

interface Repository {
    fun fetchUuidA(): Flow<String>
    fun fetchUuidB(): Flow<String>
    fun fetchUuidAB(): Flow<UnifiedUuid>
    fun start()
    fun stop()
    fun fetchGeneratorAState(): StateFlow<Boolean>
    fun fetchGeneratorBState(): StateFlow<Boolean>
    fun startStopGeneratorB(on: Boolean)
    fun startStopGeneratorA(on: Boolean)
}

@ExperimentalCoroutinesApi
class RepositoryImpl: Repository {
    private val uuidGeneratorA = UuidGenerator(4000L)
    private val uuidGeneratorB = UuidGenerator(1000L)

    val connected get() = uuidGeneratorA.connected.value && uuidGeneratorB.connected.value

    override fun start() {
//        uuidGeneratorA.start()
//        uuidGeneratorB.start()
    }

    override fun stop() {
        uuidGeneratorA.stop()
        uuidGeneratorB.stop()
    }

    override fun fetchGeneratorAState(): StateFlow<Boolean> = uuidGeneratorA.connected

    override fun fetchGeneratorBState(): StateFlow<Boolean> = uuidGeneratorB.connected

    private val flowA = callbackFlow<String> {
        val callback = object:UuidGenerator.GeneratorCallback {
            override fun onUuidChange(newUuid: String) {
                offer(newUuid)
            }
        }

        uuidGeneratorA.addCallback(callback)

        awaitClose { uuidGeneratorA.removeCallback(callback) }
    }

    private val flowB = callbackFlow<String> {
        val callback = object:UuidGenerator.GeneratorCallback {
            override fun onUuidChange(newUuid: String) {
                offer(newUuid)
            }
        }

        uuidGeneratorB.addCallback(callback)

        awaitClose { uuidGeneratorA.removeCallback(callback) }
    }

    override fun fetchUuidA(): Flow<String> = flowA

    override fun fetchUuidB(): Flow <String> = flowB

    override fun fetchUuidAB(): Flow<UnifiedUuid> = flowA
        .combine(flowB) { uuidA, uuidB ->
            UnifiedUuid(uuidA,uuidB)
        }

    override fun startStopGeneratorA(on: Boolean) {
        if (on) {
            uuidGeneratorA.start()
        } else {
            uuidGeneratorA.stop()
        }
    }

    override fun startStopGeneratorB(on: Boolean) {
        if (on) {
            uuidGeneratorB.start()
        } else {
            uuidGeneratorB.stop()
        }
    }

}

data class UnifiedUuid(val uuidA: String, val uuidB: String) {
    val unifiedUuidShortString get() = "${uuidA.substringBefore('-')}  &  ${uuidB.substringBefore('-')}"
}