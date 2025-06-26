package com.example.personaltasks.controller

import android.content.Context
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDao
import com.example.personaltasks.model.TaskFirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskController(context: Context) {
    private val taskDao: TaskDao = TaskFirebaseDatabase()

    private val scope = CoroutineScope(Dispatchers.IO)

    fun insertTask(task: Task) {
        scope.launch {
            taskDao.insert(task)
        }
    }

    fun deleteAllTasks() {
        scope.launch {
            taskDao.deleteAll()
        }
    }

    fun getAllTasks() = taskDao.getAll()

    fun deleteTask(task: Task) {
        scope.launch {
            taskDao.delete(task)
        }
    }

    fun updateTask(task: Task) {
        scope.launch {
            taskDao.update(task)
        }
    }
}