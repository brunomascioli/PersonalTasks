package com.example.personaltasks.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskRvAdapter
import com.example.personaltasks.controller.TaskController
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Task

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val tasks: MutableList<Task> = mutableListOf()

    private val taskController: TaskController by lazy {
        TaskController(this)
    }

    private val taskRvAdapter: TaskRvAdapter by lazy {
        TaskRvAdapter(tasks)
    }

    private lateinit var taskActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarIn.toolbar)

        setupRecyclerView()

        observeTasks()

        setupActivityResultLauncher()
    }

    private fun setupActivityResultLauncher() {
        taskActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            handleActivityResult(result)
        }
    }

    private fun handleActivityResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val task = result.data?.getParcelableExtra<Task>(EXTRA_TASK)
            task?.let{ receivedTask ->
                val position = tasks.indexOfFirst { it.id == receivedTask.id }
                if (position == -1) {
                    tasks.add(receivedTask)
                    taskRvAdapter.notifyItemInserted(tasks.lastIndex)
                    taskController.insertTask(receivedTask)
                    Toast(this).apply {
                        setText("Tarefa Inserida!")
                        show()
                    }
                }
                else {
                    tasks[position] = receivedTask
                    taskRvAdapter.notifyItemChanged(position)
                    taskController.updateTask(receivedTask)
                    Toast(this).apply {
                        setText("Tarefa Atualizada!")
                        show()
                    }
                }
            }
        }
    }

    private fun observeTasks() {
        taskController.getAllTasks().observe(this) { dbTasks ->
            tasks.clear()
            tasks.addAll(dbTasks)
            taskRvAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        binding.taskRv.adapter = taskRvAdapter
        binding.taskRv.layoutManager = LinearLayoutManager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add_task_mi -> {
                taskActivityResultLauncher.launch(Intent(this, TaskActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

}