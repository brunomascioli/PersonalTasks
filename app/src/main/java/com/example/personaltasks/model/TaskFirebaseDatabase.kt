package com.example.personaltasks.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TaskFirebaseDatabase : TaskDao {
    private val databaseReference = Firebase.database.getReference("taskList")
    private val taskList = mutableListOf<Task>()
    private val tasksLiveData = MutableLiveData<List<Task>>()

    init {
        databaseReference.keepSynced(true)
        
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task = snapshot.getValue<Task>()
                task?.let { newTask ->
                    if (!taskList.any { it.id == newTask.id }) {
                        taskList.add(newTask)
                        tasksLiveData.postValue(taskList.toList())
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val task = snapshot.getValue<Task>()
                task?.let { updatedTask ->
                    val index = taskList.indexOfFirst { it.id == updatedTask.id }
                    if (index != -1) {
                        taskList[index] = updatedTask
                        tasksLiveData.postValue(taskList.toList())
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val task = snapshot.getValue<Task>()
                task?.let { removedTask ->
                    taskList.removeAll { it.id == removedTask.id }
                    tasksLiveData.postValue(taskList.toList())
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
    }

    override fun getAll(): LiveData<List<Task>> {
        return tasksLiveData
    }

    override suspend fun deleteAll() {
        databaseReference.removeValue()
        taskList.clear()
        tasksLiveData.postValue(emptyList())
    }

    override suspend fun insert(task: Task) {
        databaseReference.child(task.id.toString()).setValue(task)
    }

    override suspend fun delete(task: Task) {
        databaseReference.child(task.id.toString()).removeValue()
    }

    override suspend fun update(task: Task) {
        databaseReference.child(task.id.toString()).setValue(task)
    }
}