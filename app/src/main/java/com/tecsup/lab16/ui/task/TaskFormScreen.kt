package com.tecsup.lab16.ui.task

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.lab16.data.model.Task
import com.tecsup.lab16.viewmodel.TaskViewModel

@Composable
fun TaskFormScreen(viewModel: TaskViewModel, onSave: () -> Unit) {
    val taskToEdit by viewModel.selectedTask.collectAsState()
    var title by remember(taskToEdit) { mutableStateOf(taskToEdit?.title ?: "") }
    var priority by remember(taskToEdit) { mutableStateOf(taskToEdit?.priority ?: "Medium") }
    var deadline by remember(taskToEdit) { mutableStateOf(taskToEdit?.deadline ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(if (taskToEdit == null) "Nueva Tarea" else "Editar Tarea", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = title, onValueChange = { title = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = priority, onValueChange = { priority = it }, label = { Text("Prioridad (Baja, Media, Alta)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = deadline, onValueChange = { deadline = it }, label = { Text("Fecha Límite (DD/MM/AAAA)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        
        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(onClick = {
            val dateRegex = Regex("^\\d{2}/\\d{2}/\\d{4}$")
            if (title.isBlank() || priority.isBlank() || deadline.isBlank()) {
                errorMessage = "Todos los campos son obligatorios"
            } else if (!deadline.matches(dateRegex)) {
                errorMessage = "Formato de fecha inválido. Use DD/MM/AAAA"
            } else {
                errorMessage = null
                // isCompleted is preserved from existing task or false for new
                val newTask = taskToEdit?.copy(title = title, priority = priority, deadline = deadline)
                    ?: Task(title = title, priority = priority, deadline = deadline, isCompleted = false)
                
                if (taskToEdit == null) {
                    viewModel.addTask(newTask)
                } else {
                    viewModel.updateTask(newTask)
                }
                onSave()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar Tarea")
        }
    }
}
