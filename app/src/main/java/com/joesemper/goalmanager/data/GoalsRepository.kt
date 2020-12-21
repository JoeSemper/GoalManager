package com.joesemper.goalmanager.data

import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.model.User
import kotlinx.coroutines.flow.Flow

interface GoalsRepository {
    suspend fun getCurrentUser(): User?
    fun observeGoals(): Flow<List<Goal>>
    suspend fun addOrReplaceGoal(newGoal: Goal)
    suspend fun deleteGoal(goalId: String)
}