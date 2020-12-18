package com.joesemper.goalmanager.presentation

import androidx.lifecycle.*
import com.joesemper.goalmanager.data.goalsRepository
import com.joesemper.goalmanager.model.Goal

class GoalViewModel (var goal: Goal?) : ViewModel() {

    private val showErrorLiveData = MutableLiveData<Boolean>()

    private val lifecycleOwner: LifecycleOwner = LifecycleOwner { viewModelLifecycle }
    private val viewModelLifecycle = LifecycleRegistry(lifecycleOwner).also {
        it.currentState = Lifecycle.State.RESUMED
    }


//    fun saveGoal() {
//        val goalValue = goal ?: return
//        goalsRepository.addOrReplaceGoal(goalValue)
//    }

    fun saveGoal() {
        goal?.let { note ->
            goalsRepository.addOrReplaceGoal(note).observe(lifecycleOwner) {
                it.onFailure {
                    showErrorLiveData.value = true
                }
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