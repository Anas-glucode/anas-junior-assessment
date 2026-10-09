package com.example.taskmaster.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taskmaster.domain.models.Task
import com.example.taskmaster.ui.mappers.getWeatherBackgroundRes
import com.example.taskmaster.ui.viewmodels.TaskViewModel
import com.example.taskmaster.ui.views.components.AddTaskFab
import com.example.taskmaster.ui.views.components.SearchPill
import com.example.taskmaster.ui.views.components.SearchPillHeight
import com.example.taskmaster.ui.views.components.TaskItemRow
import com.example.taskmaster.ui.views.components.TaskTabRow
import com.example.taskmaster.ui.views.components.WeatherCard
import com.example.taskmaster.ui.views.components.WeatherCardSkeleton
import com.example.taskmaster.ui.views.components.WeatherHeader

private val SearchBarVerticalMargin = 8.dp
private val Tabs = listOf("To Do", "Completed")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListView(
    navController: NavController,
    viewModel: TaskViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // State to toggle the Bottom Sheet locally on the list view
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val weatherState by viewModel.weather.collectAsState()
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    val showCompleted = selectedTabIndex == 1
    val filteredTasks = tasks.filter { task ->
        val matchesTab = task.isCompleted == showCompleted
        val matchesSearch = searchQuery.isEmpty() ||
                task.title.contains(searchQuery, ignoreCase = true) ||
                task.description.contains(searchQuery, ignoreCase = true)
        matchesTab && matchesSearch
    }

    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val listTopPadding = statusBarTop + SearchPillHeight + SearchBarVerticalMargin

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        floatingActionButton = {
            // Trigger the bottom sheet instead of navigating
            AddTaskFab(onClick = { showBottomSheet = true })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = listTopPadding,
                    bottom = innerPadding.calculateBottomPadding()
                )
            ) {
                item {
                    WeatherHeader(backgroundRes = weatherState?.getWeatherBackgroundRes() ?: com.example.taskmaster.R.drawable.sunny_weather_background) {
                        if (weatherState == null) {
                            WeatherCardSkeleton()
                        } else {
                            WeatherCard(weatherState = weatherState)
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(16.dp))
                    TaskTabRow(
                        tabs = Tabs,
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it }
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Tasks",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(10.dp))
                }

                items(
                    items = filteredTasks,
                    key = { task -> task.id ?: task.hashCode() }
                ) { task ->
                    TaskItemRow(
                        task = task,
                        onToggleComplete = {
                            viewModel.addTask(task.copy(isCompleted = !task.isCompleted))
                        },
                        onEdit = { navController.navigate("editTask/${task.id}") },
                        onDelete = { viewModel.deleteTask(task) }
                    )
                }
            }

            SearchPill(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = statusBarTop + SearchBarVerticalMargin,
                        bottom = SearchBarVerticalMargin
                    )
            )
        }

        // Bottom Sheet pops up directly over the Task List View when toggled
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                var title by remember { mutableStateOf("") }
                var description by remember { mutableStateOf("") }

                CreateTaskContent(
                    title = title,
                    description = description,
                    onTitleChange = { title = it },
                    onDescriptionChange = { description = it },
                    onCancel = { showBottomSheet = false },
                    onSave = {
                        val newTask = Task(
                            title = title.trim(),
                            description = description.trim(),
                            isCompleted = false
                        )
                        viewModel.addTask(newTask)
                        showBottomSheet = false
                    },
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }
    }
}