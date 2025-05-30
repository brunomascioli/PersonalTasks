package com.example.personaltasks.ui

interface OnTaskClickListener {
    fun onRemoveTaskMenuItemClick(position: Int)
    fun onDetailTaskItemClick(position: Int)
    fun onEditTaskMenuItemClick(position: Int)
    fun onTaskClick(position: Int)
    fun onTaskChecked(position: Int)
}