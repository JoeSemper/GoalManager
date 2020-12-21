package com.joesemper.goalmanager.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joesemper.goalmanager.data.GoalsRepository
import com.joesemper.goalmanager.errors.NoAuthException
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: GoalsRepository) : ViewModel() {
    private val viewStateLiveData = MutableLiveData<SplashViewState>()

    init {
        viewModelScope.launch {
            val user = repository.getCurrentUser()

            viewStateLiveData.value = if (user != null) {
                SplashViewState.Auth
            } else {
                SplashViewState.Error(error = NoAuthException())
            }
        }
    }

    fun observeViewState(): LiveData<SplashViewState> = viewStateLiveData


}