package com.example.taskmaster.ui.views.components.weatherTiles

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

private val sunFormatter =
    DateTimeFormatter.ofPattern("hh:mm a", Locale.US)

private fun parseSunTime(value: String): LocalTime? =
    runCatching {
        LocalTime.parse(value.trim().uppercase(Locale.US), sunFormatter)
    }.getOrNull()

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
fun SunTile(
    sunrise: String,
    sunset: String,
    modifier: Modifier = Modifier
) {
    val now by rememberNow()

    val sunriseMin = parseSunTime(sunrise)?.totalMinutes() ?: (6 * 60)
    val sunsetMin = parseSunTime(sunset)?.totalMinutes() ?: (18 * 60)
    val dayLength = (sunsetMin - sunriseMin).coerceAtLeast(1)

    val progress = (
            (now.totalMinutes() - sunriseMin).toFloat() / dayLength
            ).coerceIn(0f, 1f)

    val isDay = now.totalMinutes() in sunriseMin until sunsetMin

    var appeared by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        appeared = true
    }

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
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val baseY = size.height * 0.9f
                val rise = size.height * 0.40f

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
                    style = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                val sun = Offset(
                    x = xAt(sunProgress),
                    y = yAt(sunProgress)
                )

                val strength = if (isDay) 1f else 0.4f
                val glowRadius = 26.dp.toPx()

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFE082).copy(alpha = 0.9f * strength),
                            Color.Transparent
                        ),
                        center = sun,
                        radius = glowRadius
                    ),
                    radius = glowRadius,
                    center = sun
                )

                drawCircle(
                    color = Color(0xFFFFD54F).copy(alpha = strength),
                    radius = 9.dp.toPx(),
                    center = sun
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SunLabel(
                    label = "Today's sunrise",
                    value = sunrise,
                    horizontalAlignment = Alignment.Start
                )

                SunLabel(
                    label = "Today's sunset",
                    value = sunset,
                    horizontalAlignment = Alignment.End
                )
            }
        }
    }
}

@Composable
private fun SunLabel(
    label: String,
    value: String,
    horizontalAlignment: Alignment.Horizontal
) {
    val parts = value.trim().split(" ")

    Column(horizontalAlignment = horizontalAlignment) {
        Text(
            text = label,
            color = White90,
            fontSize = 12.sp
        )

        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(parts.first())
                }

                parts.getOrNull(1)?.let {
                    withStyle(
                        SpanStyle(
                            fontSize = 13.sp,
                            color = White70
                        )
                    ) {
                        append(" $it")
                    }
                }
            },
            color = Color.White
        )
    }
}
