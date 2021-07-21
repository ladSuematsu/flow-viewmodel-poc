package com.example.flowviewmodelpoc.fetch.viewmodel.state

sealed class FetchState {
    object NotStarted: FetchState()
    object Fetching: FetchState()
    object Canceled: FetchState()
    data class Completed(val uuid: String): FetchState()
}