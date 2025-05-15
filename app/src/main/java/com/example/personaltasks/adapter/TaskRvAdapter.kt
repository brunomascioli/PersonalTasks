package com.example.personaltasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.databinding.TaskItemBinding
import com.example.personaltasks.model.Task
import java.time.format.DateTimeFormatter

class TaskRvAdapter (
    private val tasks: MutableList<Task>,
) : RecyclerView.Adapter<TaskRvAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val tib: TaskItemBinding) :  RecyclerView.ViewHolder(tib.root)

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
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy / HH:mm")
        holder.tib.apply {
            titleTask.text = task.title
            descriptionTask.text = if (task.description.length > 50) "${task.description.subSequence(0, 50)} ..." else task.description
            dueDateTask.text = "Data limite: ${task.limitDate.format(dateFormatter)}"
        }
    }
}
