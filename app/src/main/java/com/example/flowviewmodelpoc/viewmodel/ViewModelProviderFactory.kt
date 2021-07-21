package com.example.flowviewmodelpoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flowviewmodelpoc.data.Repository
import com.example.flowviewmodelpoc.data.RepositoryImpl
import com.example.flowviewmodelpoc.data.repository.RepositoryFlow
import com.example.flowviewmodelpoc.data.repository.RepositoryFlowImpl
import com.example.flowviewmodelpoc.fetch.viewmodel.FlowViewModel
import com.example.flowviewmodelpoc.fetch.viewmodel.CoroutineJobViewModel
import com.example.flowviewmodelpoc.fetch.viewmodel.AlternativeFlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

object ViewModelProviderFactory: ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    private val repository: Repository by lazy {
        RepositoryImpl()
    }

    private val repositoryFlow: RepositoryFlow by lazy {
        RepositoryFlowImpl()
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when(modelClass) {
        CoroutineJobViewModel::class.java -> modelClass.getConstructor(RepositoryFlow::class.java)
            .newInstance(repositoryFlow)

        FlowViewModel::class.java -> modelClass.getConstructor(RepositoryFlow::class.java)
            .newInstance(repositoryFlow)

        AlternativeFlowViewModel::class.java -> modelClass.getConstructor(RepositoryFlow::class.java)
            .newInstance(repositoryFlow)

        UuidViewModel::class.java -> modelClass.getConstructor(Repository::class.java)
            .newInstance(repository)

        else -> throw Exception("could not resolve ViewModel implementation")
    } as T
}