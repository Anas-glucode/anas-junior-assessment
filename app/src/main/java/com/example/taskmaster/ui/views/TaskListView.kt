package com.example.taskmaster.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmaster.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListView() {
    // 💾 State tracking
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DockedSearchBar(
                query = searchQuery,
                onQueryChange = { newText ->
                    searchQuery = newText
                },
                onSearch = {
                    // Executed when user presses the keyboard search button
                },
                active = isSearching,
                onActiveChange = { activeState ->
                    isSearching = activeState
                },
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0096FF))
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

                        Column(horizontalAlignment = Alignment.Start
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "Sunrise",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(end = 10.dp)
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.sunrise_svgrepo_com),
                                    contentDescription = "Sunrise Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)

                                )

                                Text(
                                    text = "07:00",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(start = 10.dp)
                                )

                            }

                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "Sunset",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(end = 12.dp)
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.sunset_down_svgrepo_com),
                                    contentDescription = "Sunset Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(23.dp)
                                )

                                Text(
                                    text = "17:30",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun TaskListViewPreview() {
    MaterialTheme{
        TaskListView()
    }
}