package com.example.flowviewmodelpoc.fetch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.flowviewmodelpoc.R
import com.example.flowviewmodelpoc.databinding.FragmentFetchWaitBinding
import com.example.flowviewmodelpoc.fetch.viewmodel.state.FetchState
import com.example.flowviewmodelpoc.fetch.viewmodel.AlternativeFlowViewModel
import com.example.flowviewmodelpoc.viewmodel.ViewModelProviderFactory

class FetchWaitAlternativeFlowFragment : Fragment() {
    private lateinit var binding: FragmentFetchWaitBinding
    private lateinit var viewModel: AlternativeFlowViewModel

    private val goUpButtonListener = View.OnClickListener { view ->
        view.findNavController().navigate(R.id.action_fetchWaitFragment_to_fragmentFetchWaitHome)
    }

    private val concludeButtonListener = View.OnClickListener { view ->
        view.findNavController().navigate(R.id.action_fetchWaitFragment_to_fragmentFetchWaitConclusion)
    }

    private val tryAgainButtonListener = View.OnClickListener {
        viewModel.start(fetchObserver, viewLifecycleOwner)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFetchWaitBinding.inflate(layoutInflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.resultState) {
            labelLatestUuid.text = "Response"
            root.setOnClickListener(tryAgainButtonListener)
        }

        binding.progressBar.setOnClickListener {
            viewModel.forceStop()
        }

        viewModel = ViewModelProvider(this, ViewModelProviderFactory)
            .get(AlternativeFlowViewModel::class.java)
        viewModel.start(fetchObserver, viewLifecycleOwner)
    }

    private val fetchObserver = Observer<FetchState> { state ->
        var progressBarVisibility = View.GONE
        var textLabel = "Pending"
        var buttonLabel = "Go up"
        var buttonClickListener = goUpButtonListener
        when (state) {
            is FetchState.NotStarted -> Unit
            is FetchState.Canceled -> {
                textLabel = "Cancelled by user"
            }
            is FetchState.Fetching -> {
                progressBarVisibility = View.VISIBLE
            }
            is FetchState.Completed -> {
                textLabel = state.uuid
                buttonLabel = "Conclude"
                buttonClickListener = concludeButtonListener
            }
        }

        with (binding) {
            progressBar.visibility = progressBarVisibility
            resultState.latestUuidField.text = textLabel
            goBackHomeButton.apply {
                text = buttonLabel
                setOnClickListener(buttonClickListener)
            }
        }
    }
}
