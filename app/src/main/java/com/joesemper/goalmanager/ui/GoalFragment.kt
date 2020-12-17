package com.joesemper.goalmanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joesemper.goalmanager.R
import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.presentation.GoalViewModel
import kotlinx.android.synthetic.main.fragment_goal.*


class GoalFragment : Fragment(R.layout.fragment_goal) {
    private val goal: Goal? by lazy(LazyThreadSafetyMode.NONE){arguments?.getParcelable(GOAL_KEY)}

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GoalViewModel(goal) as T
            }
        }).get(
            GoalViewModel::class.java
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.goal.let {
            goal_title_et.setText(it?.title)
        }
    }

    companion object {
        const val GOAL_KEY = "Goal"

        fun create(goal: Goal? = null): GoalFragment {
            val fragment = GoalFragment()
            val arguments = Bundle()
            arguments.putParcelable(GOAL_KEY, goal)
            fragment.arguments = arguments
            return fragment
        }
    }
}