package com.example.taskmaster.domain.repos

import com.example.taskmaster.domain.models.Weather

interface WeatherRepository {
    suspend fun getWeather(query: String): Weather
}