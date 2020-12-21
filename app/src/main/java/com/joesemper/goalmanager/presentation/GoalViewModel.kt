package com.joesemper.goalmanager.presentation

import androidx.lifecycle.*
import com.joesemper.goalmanager.data.GoalsRepository
import com.joesemper.goalmanager.model.Goal
import kotlinx.coroutines.launch

class GoalViewModel(private val goalsRepository: GoalsRepository, var goal: Goal?) : ViewModel() {

    private val showErrorLiveData = MutableLiveData<Boolean>()


    fun saveGoal() {
        viewModelScope.launch {
            val goalValue = goal ?: return@launch

            try {
                goalsRepository.addOrReplaceGoal(goalValue)
            } catch (th: Throwable) {
                showErrorLiveData.value = true
            }
        }
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

    fun showError(): LiveData<Boolean> = showErrorLiveData
}