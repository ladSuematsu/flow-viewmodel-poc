package com.example.flowviewmodelpoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.flowviewmodelpoc.data.Repository
import com.example.flowviewmodelpoc.data.UnifiedUuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class UuidViewModel(private val repository: Repository): ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    val uuidAState = repository.fetchGeneratorAState().asLiveData()
    val uuidBState = repository.fetchGeneratorBState().asLiveData()

    val uuidA = repository.fetchUuidA()
        .flowOn(Dispatchers.IO).asLiveData()

    val uuidB = repository.fetchUuidB()
        .flowOn(Dispatchers.IO).asLiveData()

    val uuidAB: LiveData<String> = repository.fetchUuidAB()
        .map { unifiedUuid -> extractUnifiedUuidForDisplay(unifiedUuid) }
        .flowOn(Dispatchers.IO).asLiveData()

    init {
        repository.start()
    }

    fun toggleA(on: Boolean) {
        repository.startStopGeneratorA(on)
    }

    fun toggleB(on: Boolean) {
        repository.startStopGeneratorB(on)
    }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

    private fun extractUnifiedUuidForDisplay(unifiedUuid: UnifiedUuid) =
        unifiedUuid.unifiedUuidShortString
}