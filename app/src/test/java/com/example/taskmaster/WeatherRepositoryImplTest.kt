package com.example.taskmaster

import com.example.taskmaster.data.remote.api.WeatherApi
import com.example.taskmaster.data.remote.dto.ConditionDto
import com.example.taskmaster.data.remote.dto.CurrentDto
import com.example.taskmaster.data.remote.dto.LocationDto
import com.example.taskmaster.data.remote.dto.WeatherDto
import com.example.taskmaster.data.repository.WeatherRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    private lateinit var weatherApi: WeatherApi
    private lateinit var repository: WeatherRepositoryImpl

    // Built-in coroutine fake time won't work with System.currentTimeMillis()
    // so used own custom fake clock to test cache expiry logic instantly
    private var currentTimeMillis = 0L

    private val johannesburgLat = -26.2041
    private val johannesburgLon = 28.0473

    private fun sampleDto(tempC: Double = 20.0) = WeatherDto(
        location = LocationDto(
            name = "Johannesburg",
            region = "Gauteng",
            country = "South Africa",
            lat = johannesburgLat,
            lon = johannesburgLon,
            localtimeEpoch = 1_753_000_000L,
            localtime = "2026-07-24 10:00"
        ),
        current = CurrentDto(tempC = tempC, condition = ConditionDto(text = "Sunny", icon = "icon.png")),
        forecast = null
    )

    @Before
    fun setUp() {
        weatherApi = mockk()
        currentTimeMillis = 0L
        repository = WeatherRepositoryImpl(weatherApi, clock = { currentTimeMillis })
    }

    @Test
    fun whenGetWeatherFirstCalled_shouldCallApi() = runTest {
        coEvery { weatherApi.getWeather(johannesburgLat, johannesburgLon) } returns sampleDto()

        val result = repository.getWeather(johannesburgLat, johannesburgLon)

        assertEquals("Sunny", result.condition)
        coVerify(exactly = 1) { weatherApi.getWeather(johannesburgLat, johannesburgLon) }
    }

    @Test
    fun whenCalledWithinAnHour_shouldReturnCachedValue() = runTest {
        coEvery { weatherApi.getWeather(johannesburgLat, johannesburgLon) } returns sampleDto()

        repository.getWeather(johannesburgLat, johannesburgLon)
        currentTimeMillis += 30 * 60 * 1000L // 30 minutes later, still within the hour
        val second = repository.getWeather(johannesburgLat, johannesburgLon)

        assertEquals("Sunny", second.condition)
        coVerify(exactly = 1) { weatherApi.getWeather(johannesburgLat, johannesburgLon) }
    }

    @Test
    fun whenCalledJustBeforeCacheExpiry_shouldNotRefetchApi() = runTest {
        coEvery { weatherApi.getWeather(johannesburgLat, johannesburgLon) } returns sampleDto()

        repository.getWeather(johannesburgLat, johannesburgLon)
        currentTimeMillis += (60 * 60 * 1000L) - 1_000L // just under an hour
        repository.getWeather(johannesburgLat, johannesburgLon)

        coVerify(exactly = 1) { weatherApi.getWeather(johannesburgLat, johannesburgLon) }
    }

    @Test
    fun whenCacheIsOlderThanAnHour_shouldRefetchApi() = runTest {
        coEvery { weatherApi.getWeather(johannesburgLat, johannesburgLon) } returns sampleDto()

        repository.getWeather(johannesburgLat, johannesburgLon)
        currentTimeMillis += (60 * 60 * 1000L) + 1_000L // just over an hour
        repository.getWeather(johannesburgLat, johannesburgLon)

        coVerify(exactly = 2) { weatherApi.getWeather(johannesburgLat, johannesburgLon) }
    }

    @Test
    fun whenLocationChangesMeaningfully_shouldFetchNewDataFromApi() = runTest {
        val capeTownLat = -33.9249
        val capeTownLon = 18.4241

        coEvery { weatherApi.getWeather(johannesburgLat, johannesburgLon) } returns sampleDto(tempC = 20.0)
        coEvery { weatherApi.getWeather(capeTownLat, capeTownLon) } returns sampleDto(tempC = 15.0)

        repository.getWeather(johannesburgLat, johannesburgLon)
        val capeTownResult = repository.getWeather(capeTownLat, capeTownLon)

        assertEquals(15.0, capeTownResult.tempC, 0.0001)
        coVerify(exactly = 1) { weatherApi.getWeather(johannesburgLat, johannesburgLon) }
        coVerify(exactly = 1) { weatherApi.getWeather(capeTownLat, capeTownLon) }
    }

    @Test
    fun whenLocationHasTinyGpsJitter_shouldUseCachedValue() = runTest {
        coEvery { weatherApi.getWeather(any(), any()) } returns sampleDto()

        repository.getWeather(johannesburgLat, johannesburgLon)

        repository.getWeather(johannesburgLat + 0.001, johannesburgLon + 0.001)

        coVerify(exactly = 1) { weatherApi.getWeather(any(), any()) }
    }
}