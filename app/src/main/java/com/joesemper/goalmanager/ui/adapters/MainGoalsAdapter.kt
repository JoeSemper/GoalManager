package com.joesemper.goalmanager.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joesemper.goalmanager.R
import com.joesemper.goalmanager.databinding.ItemGoalBinding
import com.joesemper.goalmanager.model.Goal
import kotlinx.android.synthetic.main.item_goal.view.*

val DIF_UTIL: DiffUtil.ItemCallback<Goal> = object : DiffUtil.ItemCallback<Goal>() {
    override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return true
    }
}

class MainGoalsAdapter(val goalHandler: (Goal) -> Unit) :
    ListAdapter<Goal, MainGoalsAdapter.GoalViewHolder>(DIF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder(parent)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class GoalViewHolder(
        parent: ViewGroup,
        private val binding: ItemGoalBinding = ItemGoalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        private lateinit var currentGoal: Goal

        fun bind(item: Goal) {
            currentGoal = item
            with(binding) {
                goalTitle.text = item.title
                root.setOnClickListener(clickListener)
            }
        }

        private val clickListener: View.OnClickListener = View.OnClickListener {
            goalHandler(currentGoal)
        }
    }
}
