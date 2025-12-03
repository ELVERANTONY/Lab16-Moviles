package com.tecsup.lab16

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.lab16.ui.auth.LoginScreen
import com.tecsup.lab16.ui.auth.RegisterScreen
import com.tecsup.lab16.ui.task.TaskFormScreen
import com.tecsup.lab16.ui.task.TaskListScreen
import com.tecsup.lab16.ui.theme.Lab16Theme
import com.tecsup.lab16.viewmodel.AuthViewModel
import com.tecsup.lab16.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab16Theme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val taskViewModel: TaskViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { navController.navigate("taskList") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { navController.navigate("taskList") },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("taskList") {
            TaskListScreen(
                viewModel = taskViewModel,
                onAddTask = {
                    taskViewModel.selectTask(null)
                    navController.navigate("taskForm")
                },
                onTaskClick = { task ->
                    taskViewModel.selectTask(task)
                    navController.navigate("taskForm")
                }
            )
        }
        composable("taskForm") {
            TaskFormScreen(
                viewModel = taskViewModel,
                onSave = { navController.popBackStack() }
            )
        }
    }
}