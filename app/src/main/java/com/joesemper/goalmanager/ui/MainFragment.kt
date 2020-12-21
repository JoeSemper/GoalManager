package com.joesemper.goalmanager.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.firebase.ui.auth.AuthUI
import com.joesemper.goalmanager.R
import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.presentation.MainViewModel
import com.joesemper.goalmanager.presentation.ViewState
import com.joesemper.goalmanager.ui.adapters.MainGoalsAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main), LogoutDialog.LogoutListener {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MainGoalsAdapter {
            navigateToGoal(it)
        }

        mainRecycler.adapter = adapter

        viewModel.observeViewState().observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Value -> {
                    adapter.submitList(it.goals)
                }
                ViewState.EMPTY -> Unit
            }
        }

        fab.setOnClickListener {
            navigateToNewGoalCreation()
        }

        initToolbar()
    }


    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun navigateToGoal(goal: Goal) {
        (activity as MainActivity).navigateTo(GoalFragment.create(goal))
    }

    private fun navigateToNewGoalCreation() {
        (activity as MainActivity).navigateTo(GoalFragment.create(null))
    }

    override fun onLogout() {
        AuthUI.getInstance()
            .signOut(requireActivity())
            .addOnCompleteListener {
                startActivity(Intent(context, SplashActivity::class.java))
                requireActivity().finish()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu).let { true }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.logout -> showLogoutDialog().let { true }
            else -> false
        }


    private fun showLogoutDialog() {
        val frManager: FragmentManager = requireActivity().supportFragmentManager

        frManager.findFragmentByTag(LogoutDialog.TAG)
            ?: LogoutDialog.createInstance(this)
                .show(frManager, LogoutDialog.TAG)


    }

}

class LogoutDialog(private val fragment: LogoutListener) : DialogFragment() {
    companion object {
        val TAG = LogoutDialog::class.java.name + "TAG"
        fun createInstance(fragment: LogoutListener) = LogoutDialog(fragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context as AppCompatActivity)
            .setTitle(R.string.logout_dialog_title)
            .setMessage(R.string.logout_dialog_message)
            .setPositiveButton(getString(R.string.ok_button)) { _, _ -> fragment.onLogout() }
            .setNegativeButton(R.string.logout_dialog_cancel) { _, _ -> dismiss() }
            .create()

    interface LogoutListener {
        fun onLogout()
    }
}
