package com.example.personaltasks.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Task(
    val title: String,
    var isDone: Boolean = false,
    val description: String,
    val limitDate: LocalDateTime
) : Parcelable