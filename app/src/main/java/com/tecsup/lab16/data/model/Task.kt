package com.tecsup.lab16.data.model

import com.google.firebase.firestore.PropertyName

data class Task(
    val id: String = "",
    val title: String = "",
    val priority: String = "Medium", // Low, Medium, High
    val deadline: String = "",
    @get:PropertyName("isCompleted") @set:PropertyName("isCompleted") var isCompleted: Boolean = false,
    val userId: String = ""
)
