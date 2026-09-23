package com.example.taskmaster.ui.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmaster.R
import com.example.taskmaster.domain.models.Weather

@Composable
fun WeatherCard(
    weatherState: Weather?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = cardColors(
            containerColor = Color(0xFF0096FF)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = weatherState?.location?.name ?: "Loading...",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${weatherState?.location?.country ?: "--"} | ${weatherState?.location?.region ?: "--"}",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 16.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = weatherState?.condition ?: "",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = weatherState?.tempC?.toInt()?.toString() ?: "--",
                    color = Color.White,
                    fontSize = 100.sp,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = "°C",
                    color = Color.White,
                    fontSize = 50.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sunrise",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.width(75.dp)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.sunrise_svgrepo_com),
                        contentDescription = "Sunrise Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = weatherState?.sunrise ?: "--:--",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sunset",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.width(75.dp)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.sunset_down_svgrepo_com),
                        contentDescription = "Sunset Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = weatherState?.sunset ?: "--:--",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}