package com.joesemper.goalmanager.data

import com.joesemper.goalmanager.data.db.FireStoreDatabaseProvider
import com.joesemper.goalmanager.model.Goal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.random.Random

private val idRandom = Random(0)
val goalId: Long
    get() = idRandom.nextLong()

class MainRepository(private val provider: FireStoreDatabaseProvider) : GoalsRepository {

    override suspend fun getCurrentUser() = withContext(Dispatchers.IO) {
        provider.getCurrentUser()
    }

    override fun observeGoals(): Flow<List<Goal>> {
        return provider.observeGoals()
    }

    override suspend fun addOrReplaceGoal(newGoal: Goal) = withContext(Dispatchers.IO) {
        provider.addOrReplaceGoal(newGoal)
    }

    override suspend fun deleteGoal(goalId: String) {
        provider.deleteGoal(goalId)
    }
}