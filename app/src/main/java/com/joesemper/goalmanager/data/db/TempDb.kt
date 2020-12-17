package com.joesemper.goalmanager.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joesemper.goalmanager.model.Goal

object TempDb: DataProvider {

    var list = listOf<Goal>(Goal("Goal1"), Goal("Goal2"),
        Goal("Goal3"), Goal("Goal4"), Goal("Goal1"), Goal("Goal2"),
        Goal("Goal3"), Goal("Goal4"),Goal("Goal1"), Goal("Goal2"),
        Goal("Goal3"), Goal("Goal4"),Goal("Goal1"), Goal("Goal2"),
        Goal("Goal3"), Goal("Goal4"),)

    val data = MutableLiveData<List<Goal>>(list)

    override fun observeGoals(): LiveData<List<Goal>> {
        return data
    }
}