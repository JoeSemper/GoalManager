package com.joesemper.goalmanager.presentation

import androidx.lifecycle.*
import com.joesemper.goalmanager.data.GoalsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(goalsRepository: GoalsRepository) : ViewModel() {

    private val goalsLiveData = MutableLiveData<ViewState>()

    init {
        goalsRepository.observeGoals()
            .onEach {
                goalsLiveData.value = if (it.isEmpty()) ViewState.EMPTY else ViewState.Value(it)
            }
            .launchIn(viewModelScope)
    }

    fun observeViewState(): LiveData<ViewState> = goalsLiveData
}