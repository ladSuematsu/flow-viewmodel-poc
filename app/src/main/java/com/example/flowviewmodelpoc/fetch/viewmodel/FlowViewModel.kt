package com.example.flowviewmodelpoc.fetch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowviewmodelpoc.data.repository.RepositoryFlow
import com.example.flowviewmodelpoc.fetch.viewmodel.state.FetchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FlowViewModel(private val repository: RepositoryFlow): ViewModel() {
    private var job: Job? = null
    private var _result: MutableLiveData<FetchState> = MutableLiveData()
    val result: LiveData<FetchState> = _result

    fun start() {
        if (job?.isActive == true) {
            return
        }

        _result.apply {
            value = FetchState.Fetching
        }

        job = viewModelScope.launch {
            repository.fetch()
                .map { uuid ->
                    if (uuid.isBlank()) {
                        FetchState.Fetching
                    } else {
                        FetchState.Completed(uuid = uuid)
                    }
                }.catch {
                    emit(FetchState.Canceled)
                }.flowOn(Dispatchers.IO)
                .collect { state -> _result.value = state }
        }
    }

    fun forceStop() {
        job?.cancel()
        _result.value = FetchState.Canceled
    }
}