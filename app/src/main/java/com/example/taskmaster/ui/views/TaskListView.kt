package com.example.taskmaster.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmaster.domain.models.Task
import com.example.taskmaster.ui.theme.LocalWeatherAccent
import com.example.taskmaster.ui.theme.WeatherType
import com.example.taskmaster.ui.theme.toAccent
import com.example.taskmaster.ui.viewmodels.TaskViewModel
import com.example.taskmaster.ui.views.components.AddTaskFab
import com.example.taskmaster.ui.views.components.SearchPill
import com.example.taskmaster.ui.views.components.SearchPillHeight
import com.example.taskmaster.ui.views.components.TaskBottomSheet
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
    viewModel: TaskViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var activeTaskToEdit by remember { mutableStateOf<Task?>(null) }
    var isSheetVisible by remember { mutableStateOf(false) }
    var isWeatherViewVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val weatherState by viewModel.weather.collectAsState()
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    val weatherType = WeatherType.from(weatherState)
    val accent = remember(weatherType) { weatherType.toAccent() }

    BackHandler(enabled = isWeatherViewVisible) { isWeatherViewVisible = false }

    val showCompleted = selectedTabIndex == 1
    val filteredTasks = tasks.filter { task ->
        task.isCompleted == showCompleted && (
                searchQuery.isEmpty() ||
                        task.title.contains(searchQuery, ignoreCase = true) ||
                        task.description.contains(searchQuery, ignoreCase = true)
                )
    }

    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val listTopPadding = statusBarTop + SearchPillHeight + SearchBarVerticalMargin

    fun openSheet(task: Task?) {
        activeTaskToEdit = task
        isSheetVisible = true
    }

    CompositionLocalProvider(LocalWeatherAccent provides accent) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                floatingActionButton = { AddTaskFab(onClick = { openSheet(null) }) }
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
                            WeatherHeader(
                                backgroundRes = weatherType.backgroundRes,
                                onClick = if (weatherState != null) {
                                    { isWeatherViewVisible = true }
                                } else null
                            ) {
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
                                onEdit = { openSheet(task) },
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

                    if (isSheetVisible) {
                        TaskBottomSheet(
                            taskToEdit = activeTaskToEdit,
                            sheetState = sheetState,
                            onDismiss = { isSheetVisible = false },
                            onSave = { title, description ->
                                val base = activeTaskToEdit
                                    ?: Task(title = "", description = "", isCompleted = false)
                                viewModel.addTask(
                                    base.copy(title = title.trim(), description = description.trim())
                                )
                                isSheetVisible = false
                            }
                        )
                    }
                }
            }

            weatherState?.let { weather ->
                AnimatedVisibility(
                    visible = isWeatherViewVisible,
                    enter = fadeIn(tween(250)) + slideInVertically(tween(300)) { it / 8 },
                    exit = fadeOut(tween(200)) + slideOutVertically(tween(250)) { it / 8 }
                ) {
                    WeatherView(
                        weather = weather,
                        backgroundRes = weatherType.backgroundRes,
                        onDismiss = { isWeatherViewVisible = false }
                    )
                }
            }
        }
    }
}