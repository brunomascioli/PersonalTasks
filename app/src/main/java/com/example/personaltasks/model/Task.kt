package com.example.personaltasks.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "isDone") var isDone: Boolean = false,
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "limitDate") var limitDate: String = "",
    @ColumnInfo(name = "deleted") var deleted: Boolean = false,
    @ColumnInfo(name = "priority") var priority: TaskPriority = TaskPriority.BAIXA
) : Parcelable