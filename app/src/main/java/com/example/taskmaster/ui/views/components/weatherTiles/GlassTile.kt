package com.example.taskmaster.ui.views.components.weatherTiles

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmaster.ui.theme.LocalWeatherAccent

internal val TileShape = RoundedCornerShape(20.dp)
internal val White70 = Color.White.copy(alpha = 0.7f)
internal val White90 = Color.White.copy(alpha = 0.9f)

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
            .border(width = 0.5.dp, color = Color.White.copy(alpha = 0.25f), shape = TileShape)
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
                if (icon != null) Text(text = icon, fontSize = 14.sp)
            }
            Spacer(Modifier.height(10.dp))
        }

        content()
    }
}

@Composable
internal fun BigValue(
    value: String,
    unit: String = "",
    size: TextUnit = 30.sp
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = size,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        if (unit.isNotEmpty()) {
            Text(text = unit, fontSize = 12.sp, color = White70)
        }
    }
}

@Composable
internal fun GaugeTile(
    title: String,
    percentage: Float,
    valueText: String,
    valueSize: TextUnit,
    label: String,
    labelSpacing: Dp,
    description: String,
    modifier: Modifier = Modifier
) {
    GlassTile(title = title, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center) {
                CircularGauge(percentage = percentage, modifier = Modifier.fillMaxSize())

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = valueText,
                        color = Color.White,
                        fontSize = valueSize,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(labelSpacing))
                    Text(
                        text = label,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

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
private fun CircularGauge(
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
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val style = Stroke(stroke, cap = StrokeCap.Round)
        val startAngle = 105f
        val totalSweep = 330f

        drawArc(
            color = trackColor,
            startAngle = startAngle,
            sweepAngle = totalSweep,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = style
        )
        drawArc(
            color = progressColor,
            startAngle = startAngle,
            sweepAngle = totalSweep * percentage.coerceIn(0f, 1f),
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = style
        )
    }
}