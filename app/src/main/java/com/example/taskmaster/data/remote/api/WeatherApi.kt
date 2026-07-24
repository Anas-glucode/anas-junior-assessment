package com.example.taskmaster.data.remote.api

import com.example.taskmaster.data.remote.dto.WeatherDto

interface WeatherApi {
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherDto
}