package com.joesemper.goalmanager.data.db

import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.model.User
import kotlinx.coroutines.flow.Flow

interface DataProvider {
    fun getCurrentUser(): User?
    fun observeGoals(): Flow<List<Goal>>
    suspend fun addOrReplaceGoal(newGoal: Goal)
    suspend fun deleteGoal(goalId: String)
}