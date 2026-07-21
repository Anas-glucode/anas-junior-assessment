package com.example.taskmaster.ui.views

import android.R.attr.contentDescription
import android.R.attr.tint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taskmaster.R
import com.example.taskmaster.ui.viewmodels.TaskViewModel
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListView(navController: NavController, viewModel: TaskViewModel) {

    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("To Do") }

    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    val filteredTasks = tasks.filter { tasks ->
        if(selectedTab == "To Do") {
            !tasks.isCompleted
        } else {
            tasks.isCompleted
        }
    }

    Scaffold(
        topBar = {
            DockedSearchBar(
                query = searchQuery,
                onQueryChange = { newText -> searchQuery = newText },
                onSearch = {},
                active = isSearching,
                onActiveChange = { activeState -> isSearching = activeState },
                placeholder = { Text("Search messages") },
                leadingIcon = {
                    if (isSearching) {
                        IconButton(onClick = { isSearching = false }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                },
                trailingIcon = {
                    if (isSearching) {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear text"
                                )
                            }
                        }
                    } else {
                        IconButton(onClick = { /* Handle voice input */ }) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice search"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (searchQuery.isNotEmpty()) {
                    Text(
                        text = "Showing results for: $searchQuery",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("createTask") },
                containerColor = Color(0xFF0096FF),
                contentColor = Color.White
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add New Task",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = innerPadding
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = cardColors(containerColor = Color(0xFF0096FF))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = "Johannesburg",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "South Africa | Africa",
                                    color = Color.White.copy(alpha = 0.75F),
                                    fontSize = 16.sp,
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "20:24",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Sat May 9",
                                    color = Color.White.copy(alpha = 0.75F),
                                    fontSize = 16.sp,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "24",
                                color = Color.White,
                                fontSize = 100.sp,
                                fontWeight = FontWeight.Normal,
                            )
                            Text(
                                text = "°C",
                                color = Color.White,
                                fontSize = 50.sp,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            Text(
                                text = "Partly cloudy",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(start = 56.dp, top = 18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Column(horizontalAlignment = Alignment.Start) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sunrise",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.width(75.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.sunrise_svgrepo_com),
                                    contentDescription = "Sunrise Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "07:00",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(5.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sunset",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.width(75.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.sunset_down_svgrepo_com),
                                    contentDescription = "Sunset Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "17:30",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        FilterChip(
                            selected = selectedTab == "To Do",
                            onClick = { selectedTab = "To Do" },
                            label = {
                                Text(
                                    "To Do",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.DarkGray,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Color.Gray
                            )
                        )
                    }

                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        FilterChip(
                            selected = selectedTab == "Completed",
                            onClick = { selectedTab = "Completed" },
                            label = {
                                Text(
                                    "Completed",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.DarkGray,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Color.Gray
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Tasks",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))
            }


            items(filteredTasks) { task ->

                var isMenuExpanded by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x97E7E5E5))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0288E5))
                                        .clickable{
                                            val updatedTask = task.copy(isCompleted = !task.isCompleted)
                                            viewModel.addTask(updatedTask)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (!task.isCompleted) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(Color.White)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = task.title,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(
                                        text = task.description,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black.copy(alpha = 0.30F),
                                    )
                                }
                            }

                            Box {
                                IconButton(onClick = {isMenuExpanded = true}) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Menu to edit/delete",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = isMenuExpanded,
                                    onDismissRequest = { isMenuExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Edit")},
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Task"
                                            )
                                        },
                                        onClick = {
                                            isMenuExpanded = false
                                            navController.navigate("editTask/${task.id}")
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {Text("Delete")},
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Task",
                                                tint = Color.Red
                                            )
                                        },
                                        onClick = {
                                            isMenuExpanded = false
                                            viewModel.deleteTask(task)
                                        }

                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


