package com.joesemper.goalmanager.presentation

import androidx.lifecycle.*
import com.joesemper.goalmanager.data.GoalsRepository
import com.joesemper.goalmanager.model.Goal
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(private val goalsRepository: GoalsRepository) : ViewModel() {

    private val goalsLiveData = MutableLiveData<ViewState>()
    private val showErrorLiveData = MutableLiveData<Boolean>()

    init {
        goalsRepository.observeGoals()
            .onEach {
                goalsLiveData.value = if (it.isEmpty()) ViewState.EMPTY else ViewState.Value(it)
            }
            .launchIn(viewModelScope)
    }

    fun observeViewState(): LiveData<ViewState> = goalsLiveData


    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            try {
                goalsRepository.deleteGoal(goalId)
            } catch (th: Throwable) {
                showErrorLiveData.value = true
            }
        }
    }

    fun showError(): LiveData<Boolean> = showErrorLiveData
}