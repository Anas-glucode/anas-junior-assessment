package com.example.taskmaster.data.repository

import com.example.taskmaster.data.remote.api.WeatherApi
import com.example.taskmaster.data.remote.dto.toDomain
import com.example.taskmaster.domain.models.Weather
import com.example.taskmaster.domain.repos.WeatherRepository

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val clock: () -> Long = System::currentTimeMillis
) : WeatherRepository {

    private data class CacheEntry(
        val weather: Weather,
        val fetchedAtMillis: Long,
        val query: String
    )

    private var cache: CacheEntry? = null

    override suspend fun getWeather(query: String): Weather {
        val now = clock()
        val cached = cache

        if (cached != null &&
            cached.query == query &&
            (now - cached.fetchedAtMillis) < CACHE_DURATION_MILLIS
        ) {
            return cached.weather
        }

        val weather = weatherApi.getWeather(query).toDomain()
        cache = CacheEntry(weather = weather, fetchedAtMillis = now, query = query)
        return weather
    }

    companion object {
        private const val CACHE_DURATION_MILLIS = 60 * 60 * 1000L
    }
}