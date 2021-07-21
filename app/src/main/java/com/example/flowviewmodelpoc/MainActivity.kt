package com.example.flowviewmodelpoc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flowviewmodelpoc.activity.UuidFlowActivity
import com.example.flowviewmodelpoc.databinding.ActivityMainBinding
import com.example.flowviewmodelpoc.fetch.ui.FetchWaitActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            fetchFlowButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, FetchWaitActivity::class.java))
            }
            flowButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, UuidFlowActivity::class.java))
            }
        }
    }
}