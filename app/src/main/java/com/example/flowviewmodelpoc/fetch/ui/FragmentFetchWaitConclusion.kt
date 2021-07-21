package com.example.flowviewmodelpoc.fetch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.flowviewmodelpoc.R
import com.example.flowviewmodelpoc.databinding.FragmentFetchWaitConclusionBinding

class FragmentFetchWaitConclusion : Fragment() {

    private lateinit var binding: FragmentFetchWaitConclusionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFetchWaitConclusionBinding.inflate(layoutInflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        with(binding) {
            tryAgainButton.setOnClickListener {
                navController.navigate(R.id.action_fetchWaitFragment_to_fragmentFetchWaitHome)
            }
            goBackToHomeButton.setOnClickListener {
                requireActivity().finish()
            }
        }
    }
}