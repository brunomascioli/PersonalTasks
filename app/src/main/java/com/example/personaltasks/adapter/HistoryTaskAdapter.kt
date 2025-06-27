package com.example.personaltasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.R
import com.example.personaltasks.databinding.TaskItemBinding
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.OnHistoryTaskClickListener
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HistoryTaskAdapter(
    private val taskList: MutableList<Task>,
    private val onHistoryTaskClickListener: OnHistoryTaskClickListener
) : RecyclerView.Adapter<HistoryTaskAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnCreateContextMenuListener { menu, _, _ ->
                (onHistoryTaskClickListener as AppCompatActivity)
                    .menuInflater.inflate(R.menu.context_menu_history, menu)

                menu.findItem(R.id.reactivate_task_mi)
                    .setOnMenuItemClickListener {
                        onHistoryTaskClickListener.onReactiveTaskClick(adapterPosition)
                        true
                    }

                menu.findItem(R.id.show_task_mi)
                    .setOnMenuItemClickListener {
                        onHistoryTaskClickListener.onDetailTaskClick(adapterPosition)
                        true
                    }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {
        val binding = TaskItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val task = taskList[position]

        val instant = Instant.ofEpochMilli(task.limitDate.toLong())
        val parsedDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        holder.binding.apply {
            titleTask.text = task.title
            descriptionTask.text =
                if (task.description.length > 50)
                    "${task.description.substring(0, 50)} ..."
                else
                    task.description

            dueDateTask.text = "Data limite: $formattedDate"

            taskIsDone.isChecked = task.isDone
            taskIsDone.isEnabled = false
        }
    }
}
