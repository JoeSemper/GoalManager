package com.joesemper.goalmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.joesemper.goalmanager.R
import com.joesemper.goalmanager.databinding.EditTitleDialogBinding
import com.joesemper.goalmanager.databinding.FragmentGoalBinding
import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.presentation.GoalViewModel
import kotlinx.android.synthetic.main.edit_title_dialog.*
import kotlinx.android.synthetic.main.fragment_goal.*
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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initToolbar()

        binding.titleCard.setOnClickListener {
            val alertDialog = EditTitleDialog(binding)
            alertDialog.show(parentFragmentManager, "dlg")
        }
    }

    private fun initUi() {
        with(binding) {
            viewModel.goal?.let {
//                goalTitleEt.setText(it.title)
//                goalDescriptionEt.setText(it.description)
                goalTitle.text = it.title
                goalDescription.text = it.description
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
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        with(toolbar) {
            setNavigationOnClickListener {
                viewModel.saveGoal()
                (activity as AppCompatActivity).onBackPressed()
            }
            title = getString(R.string.new_goal_title)
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

class EditTitleDialog(private val parentBinding: FragmentGoalBinding): DialogFragment(), View.OnClickListener {

    private var _binding: EditTitleDialogBinding? = null
    private val binding: EditTitleDialogBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditTitleDialogBinding.inflate(inflater, container, false)
        binding.buttonApply.setOnClickListener(this)
        binding.buttonCancel.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.titleEt.setText(parentBinding.goalTitle.text)
        binding.descriptionEt.setText(parentBinding.goalDescription.text)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View?) {
        if ((v as Button).text == getText(R.string.apply)) {
            parentBinding.goalTitle.text = binding.titleEt.text
            parentBinding.goalDescription.text = binding.descriptionEt.text
        }
        dismiss()
    }
}