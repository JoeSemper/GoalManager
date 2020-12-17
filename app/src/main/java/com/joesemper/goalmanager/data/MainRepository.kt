package com.joesemper.goalmanager.data

import androidx.lifecycle.LiveData
import com.joesemper.goalmanager.data.db.DataProvider
import com.joesemper.goalmanager.model.Goal
import kotlin.random.Random

private val idRandom = Random(0)
val goalId: Long
    get() = idRandom.nextLong()

class MainRepository (private val provider: DataProvider): GoalsRepository {

    override fun observeGoals(): LiveData<List<Goal>> {
        return provider.observeGoals()
    }

    override fun addOrReplaceGoal(newGoal: Goal) {
        provider.addOrReplaceGoal(newGoal)
    }
}