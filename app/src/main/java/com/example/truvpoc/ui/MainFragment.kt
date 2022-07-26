package com.example.truvpoc.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.truvpoc.MainViewModel
import com.example.truvpoc.R
import com.example.truvpoc.openScreen
import com.truv.TruvBridgeView
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bridgeView = view as TruvBridgeView
        bridgeView.addEventListener(viewModel.truvEventsListener)

        viewModel.bridgeToken
            .filterNotNull()
            .onEach { bridgeToken ->
                if (!bridgeView.hasBridgeToken(bridgeToken)) {
                    bridgeView.loadBridgeTokenUrl(bridgeToken)
                }
            }
            .launchIn(lifecycleScope)

        viewModel.error
            .onEach { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
            .launchIn(lifecycleScope)

        viewModel.employmentInfo
            .filterNotNull()
            .onEach { employmentInfo ->
                requireActivity().openScreen(DisplayEmploymentFragment())
            }
            .launchIn(lifecycleScope)

        viewModel.incomeInfo
            .filterNotNull()
            .onEach { incomeInfo ->
                requireActivity().openScreen(DisplayIncomeFragment())
            }
            .launchIn(lifecycleScope)
    }

}