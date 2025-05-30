package com.example.personaltasks.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.R
import com.example.personaltasks.databinding.TaskItemBinding
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.OnTaskClickListener
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.logging.Logger

class TaskRvAdapter (
    private val onTaskClickListener: OnTaskClickListener,
    private val tasks: MutableList<Task>,
) : RecyclerView.Adapter<TaskRvAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val tib: TaskItemBinding) :  RecyclerView.ViewHolder(tib.root) {
        init {
            tib.root.setOnCreateContextMenuListener { menu, _, _ ->
                (onTaskClickListener as AppCompatActivity).menuInflater.inflate(
                    R.menu.context_menu_main, menu
                )
                menu.findItem(R.id.remove_task_mi).setOnMenuItemClickListener {
                    onTaskClickListener.onRemoveTaskMenuItemClick(adapterPosition)
                    true
                }
                menu.findItem(R.id.show_task_mi).setOnMenuItemClickListener {
                    onTaskClickListener.onDetailTaskItemClick(adapterPosition)
                    true
                }
                menu.findItem(R.id.edit_task_mi).setOnMenuItemClickListener {
                    onTaskClickListener.onEditTaskMenuItemClick(adapterPosition)
                    true
                }
            }

            tib.root.setOnClickListener {
                onTaskClickListener.onTaskClick(adapterPosition)
            }

            tib.taskIsDone.setOnClickListener {
                onTaskClickListener.onTaskChecked(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskItemBinding = TaskItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(taskItemBinding)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        val millis = task.limitDate.toLong()
        val instant = Instant.ofEpochMilli(millis)
        val parsedDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = parsedDate.format(outputFormatter)


        holder.tib.apply {
            titleTask.text = task.title
            descriptionTask.text = if (task.description.length > 50)
                "${task.description.substring(0, 50)} ..."
            else
                task.description
            dueDateTask.text = "Data limite: $formattedDate"
            taskIsDone.isChecked = task.isDone
        }
    }
}
