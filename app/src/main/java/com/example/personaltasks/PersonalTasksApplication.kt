package com.example.personaltasks

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class PersonalTasksApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        
        FirebaseDatabase.getInstance().getReference("taskList").keepSynced(true)
    }
}
