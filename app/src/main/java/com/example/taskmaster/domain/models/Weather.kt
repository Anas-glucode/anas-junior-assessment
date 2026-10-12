package com.example.taskmaster.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class DailyForecast(
    val date: String,          // yyyy-MM-dd
    val maxTempC: Double,
    val minTempC: Double,
    val condition: String,
    val chanceOfRain: Int
)

@Serializable
data class Weather(
    val location: Location,
    val tempC: Double,
    val condition: String,
    val sunrise: String,
    val sunset: String,
    val humidity: Int = 0,
    val uvIndex: Double = 0.0,
    val windKph: Double = 0.0,
    val windDegree: Int = 0,
    val windDir: String = "",
    val gustKph: Double = 0.0,
    val daily: List<DailyForecast> = emptyList()
)