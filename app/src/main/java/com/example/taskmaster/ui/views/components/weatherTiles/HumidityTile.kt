package com.example.taskmaster.ui.views.components.weatherTiles

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HumidityTile(
    humidity: Int,
    modifier: Modifier = Modifier
) {
    val (label, description) = when {
        humidity < 30 -> "Dry" to "The air is dry."
        humidity < 60 -> "Comfortable" to "Comfortable humidity."
        humidity < 80 -> "Humid" to "It feels humid."
        else -> "Very humid" to "Very humid right now."
    }

    GaugeTile(
        title = "Humidity",
        percentage = humidity / 100f,
        valueText = "$humidity%",
        valueSize = 30.sp,
        label = label,
        labelSpacing = 4.dp,
        description = description,
        modifier = modifier
    )
}