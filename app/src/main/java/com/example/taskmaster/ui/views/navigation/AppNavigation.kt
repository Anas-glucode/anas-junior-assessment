package com.example.taskmaster.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskmaster.ui.viewmodels.TaskViewModel
import com.example.taskmaster.ui.views.CreateTaskView
import com.example.taskmaster.ui.views.EditTaskView
import com.example.taskmaster.ui.views.TaskListView

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: TaskViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "taskList"
    ) {
        composable("taskList") {
            TaskListView(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("createTask") {
            CreateTaskView(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = "editTask/{taskId}",
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) {
            EditTaskView(navController = navController)
        }
    }
}