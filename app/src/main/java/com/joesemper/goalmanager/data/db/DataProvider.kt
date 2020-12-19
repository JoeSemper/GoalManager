package com.joesemper.goalmanager.data.db

import androidx.lifecycle.LiveData
import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.model.User

interface DataProvider {
    fun getCurrentUser(): User?
    fun observeGoals(): LiveData<List<Goal>>
    fun addOrReplaceGoal(newGoal: Goal): LiveData<Result<Goal>>
}