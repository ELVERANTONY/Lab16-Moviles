package com.tecsup.lab16.ui.task

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(if (taskToEdit == null) "New Task" else "Edit Task", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = priority, onValueChange = { priority = it }, label = { Text("Priority (Low, Medium, High)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = deadline, onValueChange = { deadline = it }, label = { Text("Deadline") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val newTask = taskToEdit?.copy(title = title, priority = priority, deadline = deadline)
                ?: Task(title = title, priority = priority, deadline = deadline)
            
            if (taskToEdit == null) {
                viewModel.addTask(newTask)
            } else {
                viewModel.updateTask(newTask)
            }
            onSave()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Save Task")
        }
    }
}
