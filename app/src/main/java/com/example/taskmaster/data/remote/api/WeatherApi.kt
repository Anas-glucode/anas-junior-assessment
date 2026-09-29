package com.example.taskmaster.data.remote.api

import com.example.taskmaster.data.remote.dto.WeatherDto

interface WeatherApi {
    suspend fun getWeather(query: String): WeatherDto
}