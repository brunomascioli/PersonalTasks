package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.HistoryTaskAdapter
import com.example.personaltasks.controller.TaskController
import com.example.personaltasks.databinding.ActivityHistoryBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HistoryActivity : AppCompatActivity(), OnHistoryTaskClickListener {

    private val binding: ActivityHistoryBinding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }

    private val historyTasks = mutableListOf<Task>()

    private val taskController: TaskController by lazy {
        TaskController(this)
    }

    private val historyAdapter: HistoryTaskAdapter by lazy {
        HistoryTaskAdapter(historyTasks, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Histórico"

        setupRecyclerView()
        observeRemovedTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.home_mi -> {
                finish()
                true
            }
            else -> false
        }
    }

    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser == null) finish()
    }

    private fun setupRecyclerView() = with(binding.taskRv) {
        adapter = historyAdapter
        layoutManager = LinearLayoutManager(this@HistoryActivity)
    }

    private fun observeRemovedTasks() {
        taskController.getRemovedTasks().observe(this) { removed ->
            historyTasks.clear()
            historyTasks.addAll(removed)
            historyAdapter.notifyDataSetChanged()
        }
    }

    override fun onDetailTaskClick(position: Int) {
        Intent(this, TaskActivity::class.java).apply {
            putExtra(EXTRA_TASK, historyTasks[position])
            putExtra(EXTRA_VIEW_TASK, true)
            startActivity(this)
        }
    }

    override fun onReactiveTaskClick(position: Int) {
        val task = historyTasks[position]
        task.deleted = false
        taskController.updateTask(task)
        Toast.makeText(this, "Tarefa reativada!", Toast.LENGTH_SHORT).show()
    }
}
