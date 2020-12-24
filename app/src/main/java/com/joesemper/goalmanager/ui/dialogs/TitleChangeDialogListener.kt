package com.joesemper.goalmanager.ui.dialogs

interface TitleChangeDialogListener {
    fun setTitle(title: String)
    fun setDescription(description: String)
    fun getTitle(): String
    fun getDescription(): String
}