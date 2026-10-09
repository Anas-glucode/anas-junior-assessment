package com.example.taskmaster.ui.views.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmaster.ui.viewmodels.TaskViewModel
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
            TaskListView(viewModel = viewModel)
        }
    }
}