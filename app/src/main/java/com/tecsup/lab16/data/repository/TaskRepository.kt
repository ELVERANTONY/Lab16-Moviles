package com.tecsup.lab16.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.lab16.data.model.Task
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TaskRepository {
    private val db = FirebaseFirestore.getInstance()
    private val tasksCollection = db.collection("tasks")

    fun getTasks(): Flow<List<Task>> = callbackFlow {
        val listener = tasksCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val tasks = snapshot?.toObjects(Task::class.java) ?: emptyList()
            trySend(tasks)
        }
        awaitClose { listener.remove() }
    }

    suspend fun addTask(task: Task) {
        val document = tasksCollection.document()
        val newTask = task.copy(id = document.id)
        document.set(newTask).await()
    }

    suspend fun updateTask(task: Task) {
        tasksCollection.document(task.id).set(task).await()
    }

    suspend fun deleteTask(taskId: String) {
        tasksCollection.document(taskId).delete().await()
    }
}
