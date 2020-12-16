package com.joesemper.goalmanager.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.joesemper.goalmanager.data.MainRepository
import com.joesemper.goalmanager.data.db.DataProvider
import com.joesemper.goalmanager.data.db.TempDb

class MainViewModel : ViewModel() {
    private val db: DataProvider = TempDb
    private val repository = MainRepository(db)

    fun observeViewState(): LiveData<ViewState> = repository.observeGoals()
        .map {
            if (it.isEmpty()) ViewState.EMPTY else ViewState.Value(it)
        }
}