package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityTaskBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task
import java.util.Calendar

class TaskActivity : AppCompatActivity() {
    private val binding: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    private var receivedTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarIn.toolbar)

        receivedTask = loadTaskIfExists()

        val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)

        with(binding) {
            if (receivedTask == null) {
                supportActionBar?.subtitle = "New task"
            } else if(viewTask) {
                supportActionBar?.subtitle = "View task"
                titleEt.isEnabled = false
                descEt.isEnabled = false
                datepicker.isEnabled = false
                saveBtn.visibility = View.GONE
            } else {
                supportActionBar?.subtitle = "Edit task"
            }
        }

        setupListeners()
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
                    description = descEt.text.toString(),
                    limitDate = timestamp.toString()
                )

                val resultIntent = Intent().apply {
                    putExtra(EXTRA_TASK, task)
                }

                setResult(RESULT_OK, resultIntent)
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