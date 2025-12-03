package com.tecsup.lab16.data.model

data class Task(
    val id: String = "",
    val title: String = "",
    val priority: String = "Medium", // Low, Medium, High
    val deadline: String = "",
    val isCompleted: Boolean = false,
    val userId: String = ""
)
