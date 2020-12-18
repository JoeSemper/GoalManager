package com.joesemper.goalmanager.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.joesemper.goalmanager.data.MainRepository
import com.joesemper.goalmanager.data.db.DataProvider
import com.joesemper.goalmanager.data.db.FireStoreDatabaseProvider
import com.joesemper.goalmanager.data.db.TempDb
import com.joesemper.goalmanager.data.goalsRepository

class MainViewModel : ViewModel() {
//    private val db: DataProvider = FireStoreDatabaseProvider()
//    private val repository = MainRepository(db)
//
//    fun observeViewState(): LiveData<ViewState> = repository.observeGoals()
//        .map {
//            if (it.isEmpty()) ViewState.EMPTY else ViewState.Value(it)
//        }

    fun observeViewState(): LiveData<ViewState> = goalsRepository.observeGoals()
        .map {
            if (it.isEmpty()) ViewState.EMPTY else ViewState.Value(it)
        }
}