package com.example.flowviewmodelpoc.fetch.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.flowviewmodelpoc.R

class FetchWaitActivity : AppCompatActivity(R.layout.activity_fetch_wait) {

    private lateinit var navController: NavController

    override fun onStart() {
        super.onStart()
        navController = findNavController(R.id.fragmentContainerSecondary)
    }

}