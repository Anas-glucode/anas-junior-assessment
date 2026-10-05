package com.example.taskmaster.ui.mappers

import com.example.taskmaster.R
import com.example.taskmaster.domain.models.Weather

fun Weather?.getWeatherBackgroundRes(): Int {
    val condition = this?.condition?.lowercase() ?: ""
    return when {
        condition.contains("rain") || condition.contains("drizzle") || condition.contains("storm") ->
            R.drawable.rainy_weather_background
        condition.contains("cloud") || condition.contains("overcast") || condition.contains("mist") ->
            R.drawable.cloudy_weather_background
        else ->
            R.drawable.sunny_weather_background
    }
}