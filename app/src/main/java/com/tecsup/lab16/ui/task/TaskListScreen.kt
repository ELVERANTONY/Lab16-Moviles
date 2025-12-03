package com.tecsup.lab16.ui.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.lab16.data.model.Task
import com.tecsup.lab16.viewmodel.TaskViewModel

import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskViewModel, onAddTask: () -> Unit, onTaskClick: (Task) -> Unit, onLogout: () -> Unit) {
    val context = LocalContext.current
    val tasks by viewModel.tasks.collectAsState()
    val pendingTasks = tasks.filter { !it.isCompleted }
    val completedTasks = tasks.filter { it.isCompleted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TaskManager Pro") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Tarea")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            item {
                Text(
                    text = "Pendientes",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(pendingTasks, key = { it.id }) { task ->
                TaskItem(
                    task = task, 
                    onClick = { onTaskClick(task) }, 
                    onDelete = { viewModel.deleteTask(task.id) },
                    onToggleCompletion = { viewModel.updateTask(task.copy(isCompleted = it)) },
                    context = context
                )
            }

            if (completedTasks.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Text(
                        text = "Completadas",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(completedTasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task, 
                        onClick = { onTaskClick(task) }, 
                        onDelete = { viewModel.deleteTask(task.id) },
                        onToggleCompletion = { viewModel.updateTask(task.copy(isCompleted = it)) },
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit, onDelete: () -> Unit, onToggleCompletion: (Boolean) -> Unit, context: android.content.Context) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "Prioridad: ${task.priority}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Fecha: ${task.deadline}", style = MaterialTheme.typography.bodySmall)
                Text(text = "ID: ${task.id}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { 
                    Toast.makeText(context, "Actualizando tarea...", Toast.LENGTH_SHORT).show()
                    onToggleCompletion(it) 
                }
            )
            IconButton(onClick = onClick) {
                Icon(Icons.Default.Edit, contentDescription = "Editar Tarea", tint = Color.Blue)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Tarea", tint = Color.Red)
            }
        }
    }
}
