package com.sirekanyan.knigopis.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

val EMPTY_DATE = DateModel("", "", "")

@Parcelize
data class DateModel(
    val year: String,
    val month: String,
    val day: String,
) : Parcelable {
    fun isEmpty() = this == EMPTY_DATE
}