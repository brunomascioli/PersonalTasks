package com.example.personaltasks.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table")
    fun getAll(): LiveData<List<Task>>

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)
}