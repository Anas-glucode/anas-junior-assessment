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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmaster.domain.models.Weather
import com.example.taskmaster.ui.theme.WeatherType
import com.example.taskmaster.ui.theme.toAccent
import com.example.taskmaster.ui.views.components.HumidityTile
import com.example.taskmaster.ui.views.components.SevenDayTile
import com.example.taskmaster.ui.views.components.SunTile
import com.example.taskmaster.ui.views.components.UvTile
import com.example.taskmaster.ui.views.components.WeatherCard
import com.example.taskmaster.ui.views.components.WindTiles

@Composable
fun WeatherView(
    weather: Weather,
    @DrawableRes backgroundRes: Int,
    onDismiss: () -> Unit
) {
    // Get the gradient associated with the current weather condition.
    val weatherType = remember(weather.condition) {
        WeatherType.from(weather)
    }

    val weatherAccent = weatherType.toAccent()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Weather header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .clip(RoundedCornerShape(30.dp))
            ) {
                Image(
                    painter = painterResource(id = backgroundRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                WeatherCard(weatherState = weather)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Focused View title with the dynamic weather gradient.
            Text(
                text = "Focused View",
                style = TextStyle(
                    brush = weatherAccent.brush,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(start = 10.dp)
            )

            // Seven-day forecast
            SevenDayTile(daily = weather.daily)

            // UV and humidity
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

            // Sunrise and sunset
            SunTile(
                sunrise = weather.sunrise,
                sunset = weather.sunset
            )

            // Wind speed, gusts and direction
            WindTiles(
                speedKph = weather.windKph,
                gustKph = weather.gustKph,
                degree = weather.windDegree,
                directionLabel = weather.windDir
            )

            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(30.dp)
            )
        }
    }
}