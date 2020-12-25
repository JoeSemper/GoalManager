package com.joesemper.goalmanager.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.joesemper.goalmanager.R
import com.joesemper.goalmanager.databinding.FragmentMainBinding
import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.presentation.MainViewModel
import com.joesemper.goalmanager.presentation.ViewState
import com.joesemper.goalmanager.ui.adapters.MainGoalsAdapter
import com.joesemper.goalmanager.ui.adapters.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment(), LogoutDialog.LogoutListener {

    private val viewModel by viewModel<MainViewModel>()

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    private val adapter: MainGoalsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainGoalsAdapter {
            navigateToGoal(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        observeData()
        setOnClickListeners()
        initToolbar()
    }



    private fun initRecycler() {
        binding.mainRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecycler.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(getSwipeHandler())
        itemTouchHelper.attachToRecyclerView(mainRecycler)
    }

    private fun observeData() {
        viewModel.observeViewState().observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Value -> {
                    adapter.submitList(it.goals)
                }
                ViewState.EMPTY -> Unit
            }
        }
    }


    private fun setOnClickListeners() {
        binding.fab.setOnClickListener {
            navigateToNewGoalCreation()
        }
    }

    private fun getSwipeHandler() = object : SwipeToDeleteCallback(requireContext()) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            onRecyclerItemSwiped(position)
        }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun navigateToGoal(goal: Goal) {
        (activity as MainActivity).navigateTo(GoalFragment.create(goal))
    }

    private fun deleteGoal(goalId: String) {
        viewModel.deleteGoal(goalId)
    }

    private fun navigateToNewGoalCreation() {
        (activity as MainActivity).navigateTo(GoalFragment.create(null))
    }

    private fun getItemIdByPosition(position: Int): String {
        return adapter.getCurrentItemId(position).toString()
    }

    private fun onRecyclerItemSwiped(position: Int) {
        val itemId = getItemIdByPosition(position)
        vibrate()
        callDeleteDialog(itemId)
        adapter.removeAt(position)
    }

    private fun callDeleteDialog(itemId: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.delete_dialog_message)
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(getString(R.string.ok_button)) { dialog, _ ->
                deleteGoal(itemId)
            }
        val dialog = builder.create()
        dialog.show()
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

    private fun vibrate() {
        val vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()
        val milliseconds = 100L

        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                // This method was deprecated in API level 26
                vibrator.vibrate(milliseconds)
            }
        }
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
