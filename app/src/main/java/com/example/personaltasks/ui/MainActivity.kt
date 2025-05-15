package com.example.personaltasks.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskRvAdapter
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Task
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val tasks: MutableList<Task> = mutableListOf()

    private val taskRvAdapter: TaskRvAdapter by lazy {
        TaskRvAdapter(tasks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)

        amb.taskRv.adapter = taskRvAdapter
        amb.taskRv.layoutManager = LinearLayoutManager(this)

        tasks.add(Task("Titulo1", false, "Descricao1", LocalDateTime.now().plusDays(1)))
        tasks.add(Task("Titulo2", false, "Descricao2", LocalDateTime.now().plusDays(2)))
        tasks.add(Task("Titulo3", false, "Descricao3", LocalDateTime.now().plusDays(3)))
        tasks.add(Task("Titulo4", false, "Descricao4", LocalDateTime.now().plusDays(4)))
        tasks.add(Task("Titulo5", false, "Descricao5", LocalDateTime.now().plusDays(5)))

        taskRvAdapter.notifyItemRangeInserted(0, tasks.size)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

}