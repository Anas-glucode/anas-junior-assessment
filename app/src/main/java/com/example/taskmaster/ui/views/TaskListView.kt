package com.example.taskmaster.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taskmaster.ui.viewmodels.TaskViewModel
import com.example.taskmaster.ui.views.components.TaskItemRow
import com.example.taskmaster.ui.views.components.WeatherCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListView(
    navController: NavController, viewModel: TaskViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("To Do") }

    val weatherState by viewModel.weather.collectAsState()
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchWeather(-26.183, 28.05)
    }

    val tabs = listOf("To Do", "Completed")
    val selectedTabIndex = if (selectedTab == "To Do") 0 else 1

    val filteredTasks = tasks.filter { task ->
        val matchesTab = if (selectedTab == "To Do") !task.isCompleted else task.isCompleted
        val matchesSearch = searchQuery.isEmpty() || task.title.contains(
            searchQuery, ignoreCase = true
        ) || task.description.contains(searchQuery, ignoreCase = true)

        matchesTab && matchesSearch
    }

    Scaffold(topBar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSearching) {
                    IconButton(onClick = {
                        isSearching = false
                        searchQuery = ""
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search tasks",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 16.sp
                        )
                    }

                    BasicTextField(
                        value = searchQuery, onValueChange = {
                            searchQuery = it
                            isSearching = true
                        }, singleLine = true, textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ), modifier = Modifier.fillMaxWidth()
                    )
                }

                if (isSearching && searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear text",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { navController.navigate("createTask") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add New Task",
                modifier = Modifier.size(30.dp)
            )
        }
    }) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(), contentPadding = innerPadding
        ) {
            item {
                WeatherCard(weatherState = weatherState)
            }
            item {
                SecondaryTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(
                                selectedTabIndex, matchContentSize = false
                            ), color = MaterialTheme.colorScheme.primary
                        )
                    },
                    divider = {}) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTabIndex == index
                        Tab(selected = isSelected, onClick = { selectedTab = title }, text = {
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        })
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Tasks",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            items(
                items = filteredTasks, key = { task -> task.id ?: task.hashCode() }) { task ->
                TaskItemRow(task = task, onToggleComplete = {
                    val updatedTask = task.copy(isCompleted = !task.isCompleted)
                    viewModel.addTask(updatedTask)
                }, onEdit = {
                    navController.navigate("editTask/${task.id}")
                }, onDelete = {
                    viewModel.deleteTask(task)
                })
            }
        }
    }
}