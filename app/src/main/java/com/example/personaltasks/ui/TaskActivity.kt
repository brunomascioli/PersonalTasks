package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.R
import com.example.personaltasks.databinding.ActivityTaskBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskPriority
import java.util.Calendar

class TaskActivity : AppCompatActivity() {
    private val binding: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    private var receivedTask: Task? = null

    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarIn.toolbar)

        receivedTask = loadTaskIfExists()

        val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)

        setUpTaskPrioritySpinner()

        with(binding) {
            if (receivedTask == null) {
                supportActionBar?.subtitle = "New task"
            } else if(viewTask) {
                supportActionBar?.subtitle = "View task"
                titleEt.isEnabled = false
                descEt.isEnabled = false
                datepicker.isEnabled = false
                spinner.isEnabled = false
                saveBtn.visibility = View.GONE
                cancelBtn.text = "Voltar"
            } else {
                supportActionBar?.subtitle = "Edit task"
            }

            receivedTask?.let { task ->
                titleEt.setText(task.title)
                descEt.setText(task.description)
                prioritySpinner.setSelection(task.priority.ordinal)
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = task.limitDate.toLong()
                }
                datepicker.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }
        }

        setupListeners()
    }

    private fun setUpTaskPrioritySpinner() {
        spinner = binding.prioritySpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.task_priority_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        receivedTask?.let { spinner.setSelection(it.priority.ordinal) }
    }

    private fun loadTaskIfExists(): Task? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            intent.getParcelableExtra<Task>(EXTRA_TASK)
        }
    }

    private fun setupListeners() {
        with(binding) {
            saveBtn.setOnClickListener {
                val calendar = Calendar.getInstance().apply {
                    set(datepicker.year,datepicker.month, datepicker.dayOfMonth)
                }
                val timestamp = calendar.timeInMillis
                val task = Task(
                    receivedTask?.id ?: hashCode(),
                    title = if (titleEt.text.toString().isBlank()) "Sem Título" else titleEt.text.toString(),
                    description = if (descEt.text.toString().isBlank()) "Sem Descrição" else descEt.text.toString(),
                    limitDate = timestamp.toString(),
                    priority = TaskPriority.entries[spinner.selectedItemPosition]
                )
                setResult(RESULT_OK, Intent().putExtra(EXTRA_TASK, task))
                finish()
            }

            cancelBtn.setOnClickListener {
                val resultIntent = Intent().apply {
                    setResult(RESULT_CANCELED)
                }
                finish()
            }

        }
    }
}