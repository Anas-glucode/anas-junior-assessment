package com.example.taskmaster.data.repository

import com.example.taskmaster.data.remote.api.WeatherApi
import com.example.taskmaster.domain.models.Weather
import com.example.taskmaster.domain.repos.WeatherRepository
import com.example.taskmaster.data.remote.dto.WeatherDto
import com.example.taskmaster.data.remote.dto.toDomain

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val clock: () -> Long = System::currentTimeMillis
): WeatherRepository {


    private data class CacheEntry(
        val weather: Weather,
        val fetchedAtMillis: Long,
        val latitude: Double,
        val longitude: Double
    )

    private var cache: CacheEntry? = null

    override suspend fun getWeather(latitude: Double, longitude: Double): Weather {
        val now = clock()
        val cached = cache

        val isCacheFresh = cached != null &&
                (now - cached.fetchedAtMillis) < CACHE_DURATION_MILLIS &&
                isSameLocation(cached.latitude, cached.longitude, latitude, longitude)

        if (isCacheFresh) {
            return cached!!.weather
        }

        val dto = weatherApi.getWeather(latitude, longitude)
        val weather = dto.toDomain()

        cache = CacheEntry(
            weather = weather,
            fetchedAtMillis = now,
            latitude = latitude,
            longitude = longitude
        )

        return weather
    }

    private fun isSameLocation(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Boolean {
        val threshold = 0.01
        return kotlin.math.abs(lat1 - lat2) < threshold && kotlin.math.abs(lon1 - lon2) < threshold
    }

    companion object {
        private const val CACHE_DURATION_MILLIS = 60 * 60 * 1000L
    }
}