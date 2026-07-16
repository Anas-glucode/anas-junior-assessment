package com.example.taskmaster.data.repository

import com.example.taskmaster.data.remote.api.WeatherService
import com.example.taskmaster.domain.models.Weather
import com.example.taskmaster.domain.repos.WeatherRepository
import com.example.taskmaster.data.remote.dto.WeatherDto
import com.example.taskmaster.data.remote.dto.toDomain

class WeatherRepositoryImpl(
    private val weatherService: WeatherService
): WeatherRepository {

    override suspend fun getWeather(latitude: Double, longitude: Double): Weather {
        // 1. Fetch the raw network DTO 📦
        val dto = weatherService.getWeather(latitude, longitude)

        // 2. Map it to the clean domain model and return it 🧼
        return dto.toDomain()
    }
}