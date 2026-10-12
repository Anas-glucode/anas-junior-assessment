package com.example.taskmaster.ui.views.components.weatherTiles

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmaster.domain.models.DailyForecast
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

fun conditionEmoji(condition: String): String {
    val text = condition.lowercase()

    return when {
        "thunder" in text || "storm" in text -> "⛈️"
        "rain" in text || "drizzle" in text || "shower" in text -> "🌧️"
        "snow" in text || "sleet" in text || "ice" in text -> "🌨️"
        "mist" in text || "fog" in text -> "🌫️"
        "overcast" in text -> "☁️"
        "cloud" in text -> "⛅"
        else -> "☀️"
    }
}

@Composable
fun SevenDayTile(
    daily: List<DailyForecast>,
    modifier: Modifier = Modifier
) {
    GlassTile(
        title = "${daily.size.coerceAtLeast(1)}-day forecast",
        modifier = modifier.fillMaxWidth()
    ) {
        if (daily.isEmpty()) {
            Text(
                text = "Forecast unavailable",
                color = White70,
                fontSize = 14.sp
            )
            return@GlassTile
        }

        val weekMin = daily.minOf { it.minTempC }.toFloat()
        val weekMax = daily.maxOf { it.maxTempC }.toFloat()

        daily.forEachIndexed { index, day ->
            if (index > 0) {
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.2f)
                )
            }

            val label = if (index == 0) {
                "Today"
            } else {
                runCatching {
                    LocalDate.parse(day.date)
                }.getOrNull()
                    ?.dayOfWeek
                    ?.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
                    ?: day.date
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.width(52.dp)
                )

                Column(
                    modifier = Modifier.width(44.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = conditionEmoji(day.condition),
                        fontSize = 18.sp
                    )

                    if (day.chanceOfRain >= 20) {
                        Text(
                            text = "${day.chanceOfRain}%",
                            color = Color(0xFF9BE0FF),
                            fontSize = 11.sp
                        )
                    }
                }

                Text(
                    text = "${day.minTempC.roundToInt()}°",
                    color = White70,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(36.dp)
                )

                TempBar(
                    min = day.minTempC.toFloat(),
                    max = day.maxTempC.toFloat(),
                    weekMin = weekMin,
                    weekMax = weekMax,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(5.dp)
                )

                Text(
                    text = "${day.maxTempC.roundToInt()}°",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.width(36.dp)
                )
            }
        }
    }
}

@Composable
private fun TempBar(
    min: Float,
    max: Float,
    weekMin: Float,
    weekMax: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val range = (weekMax - weekMin).coerceAtLeast(1f)
        val start = ((min - weekMin) / range * size.width)
            .coerceIn(0f, size.width)

        val end = ((max - weekMin) / range * size.width)
            .coerceIn(start, size.width)

        val radius = CornerRadius(size.height / 2)

        drawRoundRect(
            color = Color.White.copy(alpha = 0.2f),
            cornerRadius = radius
        )

        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFE6D96B),
                    Color(0xFFF59E3B)
                ),
                startX = start,
                endX = end.coerceAtLeast(start + 0.01f)
            ),
            topLeft = Offset(start, 0f),
            size = Size(
                width = (end - start).coerceAtLeast(0f),
                height = size.height
            ),
            cornerRadius = radius
        )
    }
}