package com.joesemper.goalmanager.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Goal(
    val title: String = ""
) : Parcelable
