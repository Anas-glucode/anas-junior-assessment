package com.example.taskmaster.ui.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskmaster.ui.theme.LocalWeatherAccent

@Composable
fun AddTaskFab(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val accent = LocalWeatherAccent.current
    val shape = FloatingActionButtonDefaults.shape

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .shadow(elevation = 6.dp, shape = shape)
            .background(brush = accent.brush, shape = shape),
        shape = shape,
        containerColor = Color.Transparent,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add New Task",
            modifier = Modifier.size(30.dp)
        )
    }
}