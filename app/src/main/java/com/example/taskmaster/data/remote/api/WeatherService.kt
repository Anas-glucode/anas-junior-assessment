package com.example.taskmaster.data.remote.api

import com.example.taskmaster.data.remote.dto.WeatherDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WeatherService(
    private val client: HttpClient
) {
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherDto {
        val response: WeatherDto = client
            .get("https://api.weatherapi.com/v1/current.json?key=3ed65871a29947d7991110518261607&q=$latitude,$longitude")
            .body()
        return response
    }
}