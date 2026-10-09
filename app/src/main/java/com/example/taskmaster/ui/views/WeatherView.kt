package com.example.taskmaster.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.taskmaster.domain.models.Weather
import com.example.taskmaster.ui.views.components.HumidityTile
import com.example.taskmaster.ui.views.components.SevenDayTile
import com.example.taskmaster.ui.views.components.SunTile
import com.example.taskmaster.ui.views.components.UvTile
import com.example.taskmaster.ui.views.components.WeatherCard
import com.example.taskmaster.ui.views.components.WindTiles
import org.w3c.dom.Text

@Composable
fun WeatherView(
    weather: Weather,
    @DrawableRes backgroundRes: Int,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // plain white / black page
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Header: same size and card as the task list screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .clip(RoundedCornerShape(30.dp))
            ) {
                Image(
                    painter = painterResource(id = backgroundRes),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                WeatherCard(weatherState = weather)
            }

            SevenDayTile(daily = weather.daily)

            // Two half tiles
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UvTile(
                    uv = weather.uvIndex,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                HumidityTile(
                    humidity = weather.humidity,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }

            // Full-width sun arc
            SunTile(sunrise = weather.sunrise, sunset = weather.sunset)

            // Three small tiles: speed, gusts, direction
            WindTiles(
                speedKph = weather.windKph,
                gustKph = weather.gustKph,
                degree = weather.windDegree,
                directionLabel = weather.windDir
            )

            // Forecast

            Spacer(Modifier.navigationBarsPadding().height(30.dp))
        }
    }
}