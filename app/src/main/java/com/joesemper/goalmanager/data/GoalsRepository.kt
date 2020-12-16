package com.joesemper.goalmanager.data

import androidx.lifecycle.LiveData
import com.joesemper.goalmanager.model.Goal

interface GoalsRepository {
    fun observeGoals(): LiveData<List<Goal>>
}