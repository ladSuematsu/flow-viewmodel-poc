package com.example.flowviewmodelpoc.fetch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.flowviewmodelpoc.R
import com.example.flowviewmodelpoc.databinding.FragmentFetchWaitHomeBinding

class FragmentFetchWaitHome : Fragment() {

    private lateinit var binding: FragmentFetchWaitHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFetchWaitHomeBinding.inflate(layoutInflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        with(binding) {
            flowButton.setOnClickListener {
                navController.navigate(R.id.action_fragmentFetchWaitHome_to_fetchWaitFlowFragment)
            }
            alternativeFlowButton.setOnClickListener {
                navController.navigate(R.id.action_fragmentFetchWaitHome_to_fetchWaitAlternativeFlowFragment)
            }
            jobButton.setOnClickListener {
                navController.navigate(R.id.action_fragmentFetchWaitHome_to_fetchWaitJobFragment)
            }
        }
    }
}