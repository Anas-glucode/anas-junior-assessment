package com.example.taskmaster.ui.views.components.weatherTiles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

private fun gustNote(gustKph: Double) = when {
    gustKph < 20 -> "Light"
    gustKph < 40 -> "Breezy"
    gustKph < 60 -> "Strong"
    else -> "Very strong"
}

@Composable
fun WindTiles(
    speedKph: Double,
    gustKph: Double,
    degree: Int,
    directionLabel: String,
    modifier: Modifier = Modifier
) {
    val direction = directionLabel.ifBlank { "--" }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SmallTile(
            title = "Wind",
            value = speedKph.roundToInt().toString(),
            unit = "km/h",
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        SmallTile(
            title = "Gusts",
            value = gustKph.roundToInt().toString(),
            unit = "km/h",
            note = gustNote(gustKph),
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        SmallTile(
            title = "Direction",
            value = direction,
            unit = "",
            note = "$degree°",
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}

@Composable
private fun SmallTile(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    note: String? = null
) {
    GlassTile(
        title = title,
        contentPadding = 12.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BigValue(
                value = value,
                unit = unit,
                size = 34.sp
            )

            if (!note.isNullOrEmpty()) {
                Spacer(Modifier.height(4.dp))

                Text(
                    text = note,
                    color = White70,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}