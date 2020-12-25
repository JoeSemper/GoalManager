package com.joesemper.goalmanager.model

import android.content.Context
import android.os.Parcelable
import androidx.core.content.ContextCompat
import com.joesemper.goalmanager.R
import com.joesemper.goalmanager.data.goalId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Goal(
    val id: Long = goalId,
    val title: String = "New goal",
    val description: String = "",
    val color: Color = Color.BLUE,
) : Parcelable

enum class Color(val index: Int, val designation: String) {
    WHITE(0,"White"),
    YELLOW(1,  "Yellow"),
    GREEN(2,  "Green"),
    BLUE(3,  "Blue"),
    RED(4, "Red"),
    VIOLET(5,  "Violet"),
    PINK(6,  "Pink");
}

fun Color.mapToColor(context: Context): Int {
    val id = when (this) {
        Color.WHITE -> R.color.color_white
        Color.YELLOW -> R.color.color_yellow
        Color.GREEN -> R.color.color_green
        Color.BLUE -> R.color.color_blue
        Color.RED -> R.color.color_red
        Color.VIOLET -> R.color.color_violet
        Color.PINK -> R.color.color_pink
    }
    return ContextCompat.getColor(context, id)
}

fun getArrayOfColors(): Array<String> {
    return Array(Color.values().size) { i: Int ->
        Color.values()[i].designation
    }
}

fun getColorByNumber(number: Int): Color {
    for (color in Color.values())
        if (color.index == number) {
            return color
        }
    return Color.WHITE
}

fun getRandomColor(): Color {
    return getColorByNumber((0..Color.values().size).random())
}

