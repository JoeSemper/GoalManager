package com.joesemper.goalmanager.data

import androidx.lifecycle.LiveData
import com.joesemper.goalmanager.data.db.DataProvider
import com.joesemper.goalmanager.model.Goal

class MainRepository (private val provider: DataProvider): GoalsRepository {

    override fun observeGoals(): LiveData<List<Goal>> {
        return provider.observeGoals()
    }
}