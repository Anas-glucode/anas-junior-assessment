package com.example.taskmaster.ui.views.components.weatherTiles

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
    val level = uv.coerceAtLeast(0.0).roundToInt()

    GlassTile(
        title = "UV Index",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularUvBar(
                    percentage = (uv.coerceAtLeast(0.0) / 11.0).toFloat(),
                    modifier = Modifier.fillMaxSize()
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
    trackColor: Color = Color.White.copy(alpha = 0.16f),
    progressColor: Color = Color.White,
    strokeWidth: Dp = 7.dp
) {
    Canvas(modifier = modifier) {
        val stroke = strokeWidth.toPx()
        val diameter = (size.minDimension - stroke) * 0.8f
        val arcSize = Size(diameter, diameter)

        val topLeft = Offset(
            x = (size.width - diameter) / 2f,
            y = (size.height - diameter) / 2f
        )

        val startAngle = 105f
        val totalSweep = 330f

        drawArc(
            color = trackColor,
            startAngle = startAngle,
            sweepAngle = totalSweep,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(stroke, cap = StrokeCap.Round)
        )

        drawArc(
            color = progressColor,
            startAngle = startAngle,
            sweepAngle = totalSweep * percentage.coerceIn(0f, 1f),
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(stroke, cap = StrokeCap.Round)
        )
    }
}
