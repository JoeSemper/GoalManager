package com.joesemper.goalmanager.presentation

import androidx.lifecycle.ViewModel
import com.joesemper.goalmanager.data.GoalsRepository
import com.joesemper.goalmanager.data.MainRepository
import com.joesemper.goalmanager.data.db.DataProvider
import com.joesemper.goalmanager.data.db.TempDb
import com.joesemper.goalmanager.model.Goal

class GoalViewModel (var goal: Goal?) : ViewModel() {

    private val db: DataProvider = TempDb
    private val goalsRepository: GoalsRepository = MainRepository(db)

    fun saveGoal() {
        val goalValue = goal ?: return
        goalsRepository.addOrReplaceGoal(goalValue)
    }

    fun updateTitle(newTitle: String) {
        goal = (goal ?: generateNewGoal()).copy(title = newTitle)

    }

    fun updateDescription(newDescription: String) {
        goal = (goal ?: generateNewGoal()).copy(description = newDescription)
    }

    private fun generateNewGoal(): Goal {
        return Goal()
    }

}