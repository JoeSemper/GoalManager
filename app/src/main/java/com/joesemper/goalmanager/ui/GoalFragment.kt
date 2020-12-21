package com.joesemper.goalmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.joesemper.goalmanager.databinding.FragmentGoalBinding
import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.presentation.GoalViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class GoalFragment : Fragment() {
    private val goal: Goal? by lazy(LazyThreadSafetyMode.NONE) { arguments?.getParcelable(GOAL_KEY) }

    private val viewModel by viewModel<GoalViewModel> {
        parametersOf(goal)
    }

    private var _binding: FragmentGoalBinding? = null
    private val binding: FragmentGoalBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            viewModel.goal?.let {
                goalTitleEt.setText(it.title)
                goalDescriptionEt.setText(it.description)
            }

            viewModel.showError().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Error while saving goal!", Toast.LENGTH_SHORT)
                    .show()
            }

            saveButton.setOnClickListener {
                viewModel.saveGoal()
            }

            goalTitleEt.addTextChangedListener {
                viewModel.updateTitle(it?.toString() ?: "")
            }

            goalDescriptionEt.addTextChangedListener {
                viewModel.updateDescription(it?.toString() ?: "")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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