package com.joesemper.goalmanager.data.db

import androidx.lifecycle.LiveData
import com.joesemper.goalmanager.model.Goal

interface DataProvider {

    fun observeGoals(): LiveData<List<Goal>>
    fun addOrReplaceGoal(newGoal: Goal): LiveData<Result<Goal>>
}