package com.example.taskmaster.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.taskmaster.R
import com.example.taskmaster.domain.models.Weather

enum class WeatherType(
    val start: Color,
    val end: Color,
    val backgroundRes: Int
) {
    RAINY(RainyStart, RainyEnd, R.drawable.rainy_weather_background),
    CLOUDY(CloudyStart, CloudyEnd, R.drawable.cloudy_weather_background),
    SUNNY(SunnyStart, SunnyEnd, R.drawable.sunny_weather_background);

    companion object {
        private val rainKeywords = listOf("rain", "drizzle", "storm")
        private val cloudKeywords = listOf("cloud", "overcast", "mist")

        fun from(weather: Weather?): WeatherType {
            val condition = weather?.condition?.lowercase().orEmpty()
            return when {
                rainKeywords.any { condition.contains(it) } -> RAINY
                cloudKeywords.any { condition.contains(it) } -> CLOUDY
                else -> SUNNY
            }
        }
    }
}

data class WeatherAccent(
    val brush: Brush,
    val color: Color
)

fun WeatherType.toAccent() = WeatherAccent(
    brush = Brush.linearGradient(listOf(start, end)),
    color = start
)

val LocalWeatherAccent = compositionLocalOf { WeatherType.SUNNY.toAccent() }