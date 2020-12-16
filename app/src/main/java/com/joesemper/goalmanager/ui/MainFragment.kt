package com.joesemper.goalmanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.joesemper.goalmanager.R
import com.joesemper.goalmanager.presentation.MainViewModel
import com.joesemper.goalmanager.presentation.ViewState
import com.joesemper.goalmanager.ui.adapters.MainGoalsAdapter
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(
            MainViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MainGoalsAdapter()

        mainRecycler.adapter = adapter

        viewModel.observeViewState().observe(viewLifecycleOwner) {
            when(it) {
                is ViewState.Value -> {
                    adapter.submitList(it.goals)
                }
                ViewState.EMPTY -> Unit
            }
        }
    }
}