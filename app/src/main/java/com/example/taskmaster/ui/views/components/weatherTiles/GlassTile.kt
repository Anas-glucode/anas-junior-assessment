package com.example.taskmaster.ui.views.components.weatherTiles

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
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
            .background(
                brush = accent.brush,
                shape = TileShape,
                alpha = 0.92f
            )
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = 0.25f),
                shape = TileShape
            )
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

                if (icon != null) {
                    Text(text = icon, fontSize = 14.sp)
                }
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
    size: androidx.compose.ui.unit.TextUnit = 30.sp
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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