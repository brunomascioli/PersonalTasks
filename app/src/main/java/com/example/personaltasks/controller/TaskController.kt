package com.example.personaltasks.controller

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

    fun getRemovedTasks(): LiveData<List<Task>> {
        val result = MediatorLiveData<List<Task>>()
        val source = taskDao.getAll()

        result.addSource(source) { allTasks ->
            result.value = allTasks.filter { it.deleted }
        }

        return result
    }


    fun deleteAllTasks() {
        scope.launch {
            taskDao.deleteAll()
        }
    }

    fun getAllTasks(): LiveData<List<Task>> {
        val result = MediatorLiveData<List<Task>>()
        val source = taskDao.getAll()

        result.addSource(source) { allTasks ->
            result.value = allTasks.filter { !it.deleted }
        }

        return result
    }

    fun deleteTask(task: Task) {
        scope.launch {
            task.deleted = true
            taskDao.update(task)
        }
    }

    fun updateTask(task: Task) {
        scope.launch {
            taskDao.update(task)
        }
    }
}