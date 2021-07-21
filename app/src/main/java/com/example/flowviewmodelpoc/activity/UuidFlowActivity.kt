package com.example.flowviewmodelpoc.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flowviewmodelpoc.data.Repository
import com.example.flowviewmodelpoc.data.RepositoryImpl
import com.example.flowviewmodelpoc.databinding.ActivityMainBinding
import com.example.flowviewmodelpoc.databinding.ActivityUuidFlowBinding
import com.example.flowviewmodelpoc.viewmodel.UuidViewModel

class UuidFlowActivity : AppCompatActivity() {
    lateinit var binding: ActivityUuidFlowBinding

    private val viewModel: UuidViewModel by lazy {
        ViewModelProvider(this, ViewModelProviderFactory())
            .get(UuidViewModel::class.java)
    }

    private val uuidAObserver = Observer<String> { newUuid ->
        binding.uuidAEntry.latestUuidField.text = newUuid
    }

    private val uuidBObserver = Observer<String> { newUuid ->
        binding.uuidBEntry.latestUuidField.text = newUuid
    }

    private val uuidAbObserver = Observer<String> { unifiedUuidDisplay ->
        binding.uuidAbEntry.latestUuidField.text = unifiedUuidDisplay
    }

    private val uuidAStateObserver = Observer<Boolean> { isOn ->
        binding.controlPanel.uuidAGenerator.isChecked = isOn
    }

    private val uuidBStateObserver = Observer<Boolean> { isOn ->
        binding.controlPanel.uuidAGenerator.isChecked = isOn
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUuidFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uuidAEntry.labelLatestUuid.text = "UUID A"
        binding.uuidBEntry.labelLatestUuid.text = "UUID B"
        binding.uuidAbEntry.labelLatestUuid.text = "UUID AB"

        binding.controlPanel.uuidAGenerator
            .setOnCheckedChangeListener { _, isActive ->
                viewModel.toggleA(isActive)
            }

        binding.controlPanel.uuidBGenerator
            .setOnCheckedChangeListener { _, isActive ->
                viewModel.toggleB(isActive)
            }
    }

    override fun onStart() {
        super.onStart()
        viewModel.uuidA.observe(this, uuidAObserver)
        viewModel.uuidB.observe(this, uuidBObserver)
        viewModel.uuidAB.observe(this, uuidAbObserver)
        viewModel.uuidAState.observe(this, uuidAStateObserver)
        viewModel.uuidBState.observe(this, uuidBStateObserver)
    }
}

class ViewModelProviderFactory: ViewModelProvider.Factory {
    private val repository = RepositoryImpl()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(Repository::class.java).newInstance(repository)
}