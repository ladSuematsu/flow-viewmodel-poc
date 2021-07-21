package com.example.flowviewmodelpoc.fetch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowviewmodelpoc.data.repository.RepositoryFlow
import com.example.flowviewmodelpoc.fetch.viewmodel.state.FetchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CoroutineJobViewModel(private val repository: RepositoryFlow): ViewModel() {
    private var _result: MutableLiveData<FetchState> = MutableLiveData()
    val result: LiveData<FetchState> = _result

    private var job: Job? = null

    fun start() {
        if (job?.isActive == true) {
            return
        }

        _result.apply {
            value = FetchState.Fetching
        }

        job = viewModelScope.launch {
            repository.generateJob { uuid ->
                val newState = if (uuid.isBlank()) {
                    FetchState.Fetching
                } else {
                    FetchState.Completed(uuid = uuid)
                }

                this@CoroutineJobViewModel.viewModelScope
                    _result.postValue(newState)

            }
        }
    }

    fun forceStop() {
        job?.cancel()
        _result?.value = FetchState.Canceled
    }
}