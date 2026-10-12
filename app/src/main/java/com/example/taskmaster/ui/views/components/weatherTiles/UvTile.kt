package com.example.taskmaster.ui.views.components.weatherTiles

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.roundToInt

private fun uvLabel(uv: Int) = when {
    uv <= 2 -> "Low"
    uv <= 5 -> "Moderate"
    uv <= 7 -> "High"
    uv <= 10 -> "Very high"
    else -> "Extreme"
}

private fun uvAdvice(uv: Int) = when {
    uv <= 2 -> "No protection needed."
    uv <= 5 -> "Wear sunscreen outdoors."
    uv <= 7 -> "Cover up and use SPF 30+."
    uv <= 10 -> "Limit time in the sun."
    else -> "Avoid the sun if you can."
}

@Composable
fun UvTile(
    uv: Double,
    modifier: Modifier = Modifier
) {
    val clamped = uv.coerceAtLeast(0.0)
    val level = clamped.roundToInt()

    GaugeTile(
        title = "UV Index",
        percentage = (clamped / 11.0).toFloat(),
        valueText = if (uv % 1.0 == 0.0) uv.roundToInt().toString() else "%.1f".format(Locale.US, uv),
        valueSize = 34.sp,
        label = uvLabel(level),
        labelSpacing = 3.dp,
        description = uvAdvice(level),
        modifier = modifier
    )
}