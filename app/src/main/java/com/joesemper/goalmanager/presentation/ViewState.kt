package com.joesemper.goalmanager.presentation

import com.joesemper.goalmanager.model.Goal

sealed class ViewState {
    data class Value(val goals: List<Goal>) : ViewState()
    object EMPTY : ViewState()
}