package com.joesemper.goalmanager.model

import android.os.Parcelable
import com.joesemper.goalmanager.data.goalId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Goal(
    val id: Long = goalId,
    val title: String = "New note",
    val description: String = ""
) : Parcelable
