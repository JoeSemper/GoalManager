package com.joesemper.goalmanager.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joesemper.goalmanager.model.Goal

object TempDb : DataProvider {

    private val _list = mutableListOf(Goal(title = "Sample goal"))
    private val list: List<Goal> = _list

    private val data = MutableLiveData(list)

    override fun observeGoals(): LiveData<List<Goal>> {
        return data
    }

    override fun addOrReplaceGoal(newGoal: Goal) {

        for (goal in _list) {
            if (goal.id == newGoal.id) {
                _list.remove(goal)
            }
            _list.add(_list.size, newGoal)
        }

    }
}