package com.example.personaltasks.ui

sealed interface OnHistoryTaskClickListener {
    fun onDetailTaskClick(position: Int)
    fun onReactiveTaskClick(position: Int)
}