package com.example.taskmaster.domain.repos

import com.example.taskmaster.domain.models.Weather

interface WeatherRepository {

    suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): Weather
}