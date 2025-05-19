package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskRvAdapter
import com.example.personaltasks.controller.TaskController
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Task
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val tasks: MutableList<Task> = mutableListOf()

    private val taskController: TaskController by lazy {
        TaskController(this)
    }

    private val taskRvAdapter: TaskRvAdapter by lazy {
        TaskRvAdapter(tasks)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)

        amb.taskRv.adapter = taskRvAdapter
        amb.taskRv.layoutManager = LinearLayoutManager(this)

        val sampleTasks = listOf(
            Task(title = "Comprar supermercado", description = "Comprar leite, pão e ovos", limitDate =  LocalDateTime.now().plusDays(1).toString()),
            Task(title ="Estudar Kotlin", description = "Fazer exercícios sobre listas e lambdas", limitDate =  LocalDateTime.now().plusDays(2).toString()),
            Task(title = "Exercício físico", description = "Correr 5 km no parque",  limitDate = LocalDateTime.now().plusHours(3).toString())
        )

        sampleTasks.forEach { taskController.insertTask(it) }

        taskController.getAllTasks().observe(this) { dbTasks ->
            tasks.clear()
            tasks.addAll(dbTasks)
            taskRvAdapter.notifyDataSetChanged()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

}