package com.example.taskmaster.ui.views.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmaster.R
import com.example.taskmaster.domain.models.DailyForecast
import com.example.taskmaster.ui.theme.LocalWeatherAccent
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

private val TileShape = RoundedCornerShape(20.dp)
private val White70 = Color.White.copy(alpha = 0.7f)
private val White90 = Color.White.copy(alpha = 0.9f)

@Composable
fun GlassTile(
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: String? = null,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val accent = LocalWeatherAccent.current
    Column(
        modifier = modifier
            .clip(TileShape)
            .background(brush = accent.brush, shape = TileShape, alpha = 0.92f)
            .border(0.5.dp, Color.White.copy(alpha = 0.25f), TileShape)
            .padding(contentPadding)
    ) {
        if (title != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = White90,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1
                )
                if (icon != null) Text(icon, fontSize = 14.sp)
            }
            Spacer(Modifier.height(10.dp))
        }
        content()
    }
}

@Composable
private fun BigValue(value: String, unit: String = "", size: TextUnit = 30.sp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = size,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        if (unit.isNotEmpty()) {
            Text(
                text = unit,
                fontSize = 12.sp,
                color = White70
            )
        }
    }
}

// ───────────────────────── UV index ─────────────────────────

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
    val level = uv.coerceAtLeast(0.0).roundToInt()

    val uvColor = when {
        level <= 2 -> Color(0xFF72E6A6)      // Low: green
        level <= 5 -> Color(0xFFFFD166)      // Moderate: yellow
        level <= 7 -> Color(0xFFFFA45B)      // High: orange
        level <= 10 -> Color(0xFFFF6B6B)     // Very high: red
        else -> Color(0xFFCB9BFF)            // Extreme: purple
    }

    GlassTile(title = "UV Index", modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularUvBar(
                    percentage = (uv.coerceAtLeast(0.0) / 11.0).toFloat(),
                    modifier = Modifier.fillMaxSize(),
                    progressColor = uvColor,
                    trackColor = Color.White.copy(alpha = 0.16f),
                    strokeWidth = 7.dp
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (uv % 1.0 == 0.0) {
                            uv.roundToInt().toString()
                        } else {
                            "%.1f".format(Locale.US, uv)
                        },
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = uvLabel(level),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = uvAdvice(level),
                color = White70,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CircularUvBar(
    percentage: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Color.White.copy(alpha = 0.2f),
    progressColor: Color = Color(0xFFFFA45B),
    strokeWidth: Dp = 7.dp
) {
    Canvas(modifier = modifier) {
        val stroke = strokeWidth.toPx()
        val diameter = size.minDimension - stroke

        val arcSize = Size(diameter, diameter)

        val topLeft = Offset(
            x = (size.width - diameter) / 2f,
            y = (size.height - diameter) / 2f
        )

        val startAngle = 105f
        val totalSweep = 330f

        // Background ring
        drawArc(
            color = trackColor,
            startAngle = startAngle,
            sweepAngle = totalSweep,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(
                width = stroke,
                cap = StrokeCap.Round
            )
        )

        // UV level progress
        drawArc(
            color = progressColor,
            startAngle = startAngle,
            sweepAngle = totalSweep * percentage.coerceIn(0f, 1f),
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(
                width = stroke,
                cap = StrokeCap.Round
            )
        )
    }
}

// ───────────────────────── Humidity ─────────────────────────

@Composable
fun HumidityTile(
    humidity: Int,
    modifier: Modifier = Modifier
) {
    val label = when {
        humidity < 30 -> "Dry"
        humidity < 60 -> "Comfortable"
        humidity < 80 -> "Humid"
        else -> "Very humid"
    }

    val description = when {
        humidity < 30 -> "The air is dry."
        humidity < 60 -> "Comfortable humidity."
        humidity < 80 -> "It feels humid."
        else -> "Very humid right now."
    }

    GlassTile(title = "Humidity", modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularHumidityBar(
                    percentage = humidity / 100f,
                    modifier = Modifier.fillMaxSize(),
                    progressColor = Color.White,
                    trackColor = Color.White.copy(alpha = 0.16f),
                    strokeWidth = 7.dp
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$humidity%",
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = label,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = description,
                color = White70,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CircularHumidityBar(
    percentage: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Color.White.copy(alpha = 0.18f),
    progressColor: Color = Color.White,
    strokeWidth: Dp = 7.dp
) {
    Canvas(modifier = modifier) {
        val stroke = strokeWidth.toPx()
        val diameter = size.minDimension - stroke
        val arcSize = Size(diameter, diameter)

        val topLeft = Offset(
            x = (size.width - diameter) / 2f,
            y = (size.height - diameter) / 2f
        )

        val startAngle = 105f
        val totalSweep = 330f

        // Subtle translucent white background ring.
        drawArc(
            color = trackColor,
            startAngle = startAngle,
            sweepAngle = totalSweep,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(
                width = stroke,
                cap = StrokeCap.Round
            )
        )

        // Solid white arc representing the humidity percentage.
        drawArc(
            color = progressColor,
            startAngle = startAngle,
            sweepAngle = totalSweep * percentage.coerceIn(0f, 1f),
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(
                width = stroke,
                cap = StrokeCap.Round
            )
        )
    }
}

// ───────────────────────── Sunrise / sunset ─────────────────────────

private val sunFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)

private fun parseSunTime(value: String): LocalTime? =
    runCatching { LocalTime.parse(value.trim().uppercase(Locale.US), sunFormatter) }.getOrNull()

private fun LocalTime.totalMinutes() = hour * 60 + minute

@Composable
private fun rememberNow(): State<LocalTime> =
    produceState(initialValue = LocalTime.now()) {
        while (true) {
            value = LocalTime.now()
            delay(60_000.milliseconds)
        }
    }

@Composable
fun SunTile(sunrise: String, sunset: String, modifier: Modifier = Modifier) {
    val now by rememberNow()

    val sunriseMin = parseSunTime(sunrise)?.totalMinutes() ?: (6 * 60)
    val sunsetMin = parseSunTime(sunset)?.totalMinutes() ?: (18 * 60)
    val dayLength = (sunsetMin - sunriseMin).coerceAtLeast(1)

    // 0 = sunrise, 1 = sunset. At night the sun rests at the nearest end, dimmed.
    val progress = ((now.totalMinutes() - sunriseMin).toFloat() / dayLength).coerceIn(0f, 1f)
    val isDay = now.totalMinutes() in sunriseMin until sunsetMin

    // Glide from sunrise to the current position when the tile appears
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { appeared = true }
    val sunProgress by animateFloatAsState(
        targetValue = if (appeared) progress else 0f,
        animationSpec = tween(durationMillis = 1800),
        label = "sunProgress"
    )

    GlassTile(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val baseY = size.height * 0.9f
                val rise = size.height * 0.40f

                // Parabola from bottom-left to bottom-right, peak in the middle
                fun xAt(t: Float) = t * w
                fun yAt(t: Float) = baseY - 4f * rise * t * (1f - t)

                val arc = Path().apply {
                    moveTo(0f, baseY)
                    quadraticTo(w / 2f, baseY - 2f * rise, w, baseY)
                }
                drawPath(
                    path = arc,
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color(0xFFFFE9A8),
                            Color.White.copy(alpha = 0.15f)
                        )
                    ),
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                val sun = Offset(xAt(sunProgress), yAt(sunProgress))
                val strength = if (isDay) 1f else 0.4f
                val glowRadius = 26.dp.toPx()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFE082).copy(alpha = 0.9f * strength), Color.Transparent),
                        center = sun,
                        radius = glowRadius
                    ),
                    radius = glowRadius,
                    center = sun
                )
                drawCircle(Color(0xFFFFD54F).copy(alpha = strength), radius = 9.dp.toPx(), center = sun)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SunLabel("Today's sunrise", sunrise, Alignment.Start)
                SunLabel("Today's sunset", sunset, Alignment.End)
            }
        }
    }
}

@Composable
private fun SunLabel(
    label: String,
    value: String,
    horizontalAlignment: Alignment.Horizontal,
) {
    val parts = value.trim().split(" ")
    Column(horizontalAlignment = horizontalAlignment) {
        Text(label, color = White90, fontSize = 12.sp)
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)) {
                    append(parts.first())
                }
                parts.getOrNull(1)?.let {
                    withStyle(SpanStyle(fontSize = 13.sp, color = White70)) { append(" $it") }
                }
            },
            color = Color.White
        )
    }
}

// ───────────────────────── Wind (three small tiles) ─────────────────────────

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
    val dir = directionLabel.ifBlank { "--" }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SmallTile(
            title = "Wind",
            value = "${speedKph.roundToInt()}", unit = "km/h",
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
        SmallTile(
            title = "Gusts",
            value = "${gustKph.roundToInt()}", unit = "km/h",
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
        SmallTile(
            title = "Direction",
            value = dir, unit = "",
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
    note: String? = null,
) {
    GlassTile(title = title, contentPadding = 12.dp, modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BigValue(value = value, unit = unit, size = 34.sp)

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

// ───────────────────────── Forecast ─────────────────────────

fun conditionEmoji(condition: String): String {
    val s = condition.lowercase()
    return when {
        "thunder" in s || "storm" in s -> "⛈️"
        "rain" in s || "drizzle" in s || "shower" in s -> "🌧️"
        "snow" in s || "sleet" in s || "ice" in s -> "🌨️"
        "mist" in s || "fog" in s -> "🌫️"
        "overcast" in s -> "☁️"
        "cloud" in s -> "⛅"
        else -> "☀️"
    }
}

@Composable
fun SevenDayTile(daily: List<DailyForecast>, modifier: Modifier = Modifier) {
    GlassTile(
        title = "${daily.size.coerceAtLeast(1)}-day forecast",
        modifier = modifier.fillMaxWidth()
    ) {
        if (daily.isEmpty()) {
            Text("Forecast unavailable", color = White70, fontSize = 14.sp)
            return@GlassTile
        }
        val weekMin = daily.minOf { it.minTempC }.toFloat()
        val weekMax = daily.maxOf { it.maxTempC }.toFloat()

        daily.forEachIndexed { index, day ->
            if (index > 0) HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            val label = if (index == 0) "Today" else {
                runCatching { LocalDate.parse(day.date) }.getOrNull()
                    ?.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.getDefault())
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
                    Text(conditionEmoji(day.condition), fontSize = 18.sp)
                    if (day.chanceOfRain >= 20) {
                        Text("${day.chanceOfRain}%", color = Color(0xFF9BE0FF), fontSize = 11.sp)
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
        val start = (min - weekMin) / range * size.width
        val end = ((max - weekMin) / range * size.width).coerceAtLeast(start + size.height)
        val radius = CornerRadius(size.height / 2)

        drawRoundRect(color = Color.White.copy(alpha = 0.2f), cornerRadius = radius)
        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color(0xFFE6D96B), Color(0xFFF59E3B)),
                startX = start,
                endX = end
            ),
            topLeft = Offset(start, 0f),
            size = Size(end - start, size.height),
            cornerRadius = radius
        )
    }
}