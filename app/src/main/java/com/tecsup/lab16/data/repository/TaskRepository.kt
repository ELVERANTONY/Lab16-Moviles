package com.tecsup.lab16.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.lab16.data.model.Task
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

import com.google.firebase.auth.FirebaseAuth

class TaskRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val tasksCollection = db.collection("tasks")

    fun getTasks(): Flow<List<Task>> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            close(Exception("User not logged in"))
            return@callbackFlow
        }

        val listener = tasksCollection.whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
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
        val userId = auth.currentUser?.uid ?: return
        val document = tasksCollection.document()
        val newTask = task.copy(id = document.id, userId = userId)
        document.set(newTask).await()
    }

    suspend fun updateTask(task: Task) {
        tasksCollection.document(task.id).set(task).await()
    }

    suspend fun deleteTask(taskId: String) {
        tasksCollection.document(taskId).delete().await()
    }
}
