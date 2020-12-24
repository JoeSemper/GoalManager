package com.joesemper.goalmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.joesemper.goalmanager.R
import com.joesemper.goalmanager.databinding.FragmentGoalBinding
import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.presentation.GoalViewModel
import com.joesemper.goalmanager.ui.dialogs.EditTitleDialog
import com.joesemper.goalmanager.ui.dialogs.TitleChangeDialogListener
import kotlinx.android.synthetic.main.fragment_goal.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class GoalFragment : Fragment(), TitleChangeDialogListener {
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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initToolbar()
        setOnClickListeners()
    }

    private fun initUi() {
        with(binding) {
            viewModel.goal?.let {
                goalTitle.text = it.title
                if (it.description != "") {
                    goalDescription.text = it.description
                } else {
                    goalDescription.visibility = View.GONE
                }
            }

            viewModel.showError().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Error while saving goal!", Toast.LENGTH_SHORT)
                    .show()
            }

            saveButton.setOnClickListener {
                viewModel.saveGoal()
            }

            goalTitle.addTextChangedListener {
                viewModel.updateTitle(it?.toString() ?: "")
            }

            goalDescription.addTextChangedListener {
                viewModel.updateDescription(it?.toString() ?: "")
            }
        }
    }

    private fun initToolbar() {
        with(activity as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        with(toolbar) {
            setNavigationOnClickListener {
                viewModel.saveGoal()
                requireActivity().onBackPressed()
            }
            title = getString(R.string.new_goal_title)
        }
    }

    private fun setOnClickListeners() {
        binding.titleCard.setOnClickListener {
            val alertDialog = EditTitleDialog(this)
            alertDialog.show(parentFragmentManager, "dlg")
        }
    }

    override fun setTitle(title: String) {
        binding.goalTitle.text = title
    }

    override fun setDescription(description: String) {
        with(binding) {
            if(description != "") {
                goalDescription.visibility =  View.VISIBLE
                goalDescription.text = description
            } else {
                goalDescription.visibility =  View.GONE
                goalDescription.text = description
            }
        }

    }

    override fun getTitle(): String {
        return binding.goalTitle.text.toString()
    }

    override fun getDescription(): String {
        return binding.goalDescription.text.toString()
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