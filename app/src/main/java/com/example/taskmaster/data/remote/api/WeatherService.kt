package com.example.taskmaster.data.remote.api

import com.example.taskmaster.BuildConfig
import com.example.taskmaster.data.remote.dto.WeatherDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WeatherService(
    private val client: HttpClient
) : WeatherApi {
    override suspend fun getWeather(query: String): WeatherDto {
        return client.get("https://api.weatherapi.com/v1/forecast.json") {
            parameter("key", BuildConfig.WEATHER_API_KEY)
            parameter("q", query)
            parameter("days", 1)
        }.body()
    }
}