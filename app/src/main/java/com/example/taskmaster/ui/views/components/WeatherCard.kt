package com.example.taskmaster.ui.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
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
    val primaryTextColor = Color(0xFF2D2357)

    val currentDay = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()).uppercase()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = currentDay,
                    color = primaryTextColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.9.sp
                )

                Text(
                    text = (weatherState?.condition ?: "Loading...").uppercase(),
                    color = primaryTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.45.sp
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${weatherState?.tempC?.toInt() ?: 20}°",
                        color = primaryTextColor,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(7.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sunrise_svgrepo_com),
                                contentDescription = "Sunrise Icon",
                                tint = primaryTextColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(9.dp))

                            Text(
                                text = "Sunrise:",
                                color = primaryTextColor.copy(alpha = 0.8f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.width(58.dp)
                            )
                            Text(
                                text = weatherState?.sunrise ?: "--:--",
                                color = primaryTextColor.copy(alpha = 0.8f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sunset_down_svgrepo_com),
                                contentDescription = "Sunset Icon",
                                tint = primaryTextColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(9.dp))
                            Text(
                                text = "Sunset:",
                                color = primaryTextColor.copy(alpha = 0.8f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.width(58.dp)
                            )
                            Text(
                                text = weatherState?.sunset ?: "--:--",
                                color = primaryTextColor.copy(alpha = 0.8f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${weatherState?.location?.name ?: "--"}, ${weatherState?.location?.country ?: "--"}",
                        color = primaryTextColor,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun WeatherCardPreview() {
    val sampleWeather = Weather(
        location = Location(
            name = "New York",
            country = "United States",
            region = "New York",
            lat = 40.7128,
            lon = -74.0060,
            localtime = "2026-10-05 10:21",
            localtimeEpoch = 1759650060L
        ),
        tempC = 20.0,
        condition = "Heavy Rain",
        sunrise = "06:15 AM",
        sunset = "07:45 PM"
    )

    WeatherCard(weatherState = sampleWeather)
}