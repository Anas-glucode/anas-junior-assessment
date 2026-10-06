package com.example.taskmaster.ui.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmaster.R
import com.example.taskmaster.domain.models.Location
import com.example.taskmaster.domain.models.Weather
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeatherCard(
    weatherState: Weather?,
    modifier: Modifier = Modifier
) {
    val primaryTextColor = Color(0xFFFFFFFF)
    val currentDay = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()).uppercase()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp, vertical = 50.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "${weatherState?.tempC?.toInt() ?: 20}°",
                    color = primaryTextColor,
                    fontSize = 90.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 72.sp
                )

                Text(
                    text = (weatherState?.condition ?: "Loading...").uppercase(),
                    color = primaryTextColor.copy(alpha = 0.9f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
            }

            Text(
                text = currentDay,
                color = primaryTextColor.copy(alpha = 0.9f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.sunrise_svgrepo_com),
                contentDescription = "Sunrise Icon",
                tint = primaryTextColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = weatherState?.sunrise ?: "--:--",
                color = primaryTextColor.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Icon(
                painter = painterResource(id = R.drawable.sunset_down_svgrepo_com),
                contentDescription = "Sunset Icon",
                tint = primaryTextColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = weatherState?.sunset ?: "--:--",
                color = primaryTextColor.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "${weatherState?.location?.name ?: "--"}, ${weatherState?.location?.country ?: "--"}".uppercase(),
            color = primaryTextColor.copy(alpha = 0.9f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF4A90E2)
@Composable
fun WeatherCardPreview() {
    val sampleWeather = Weather(
        location = Location(
            name = "London",
            country = "United Kingdom",
            region = "London",
            lat = 51.5074,
            lon = -0.1278,
            localtime = "2026-10-06 10:21",
            localtimeEpoch = 1759650060L
        ),
        tempC = 3.0,
        condition = "Rainy",
        sunrise = "06:15 AM",
        sunset = "07:45 PM"
    )

    WeatherCard(weatherState = sampleWeather)
}