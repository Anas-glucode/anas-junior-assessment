package com.example.taskmaster

import com.example.taskmaster.domain.models.Location
import com.example.taskmaster.domain.models.Task
import com.example.taskmaster.domain.models.Weather
import com.example.taskmaster.domain.repos.TaskRepository
import com.example.taskmaster.domain.repos.WeatherRepository
import com.example.taskmaster.domain.usecases.GetTasksWithWeatherUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class GetTasksWithWeatherUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var useCase: GetTasksWithWeatherUseCase

    private val latitude = -26.2041
    private val longitude = 28.0473

    private fun sampleTask(id: Int = 1) = Task(
        id = id,
        title = "Buy milk",
        description = "2% at the store",
        isCompleted = false
    )

    private fun sampleWeather() = Weather(
        location = Location(
            name = "Johannesburg",
            region = "Gauteng",
            country = "South Africa",
            lat = latitude,
            lon = longitude,
            localtimeEpoch = 1_753_000_000L,
            localtime = "2026-07-24 10:00"
        ),
        tempC = 18.5,
        condition = "Partly cloudy",
        sunrise = "06:12 AM",
        sunset = "05:32 PM"
    )

    @Before
    fun setUp() {
        taskRepository = mockk()
        weatherRepository = mockk()
        useCase = GetTasksWithWeatherUseCase(taskRepository, weatherRepository)
    }

    @Test
    fun whenTaskExists_invoke_shouldReturnTaskWithWeather() = runTest {
        coEvery { taskRepository.getTaskById(1) } returns sampleTask()
        coEvery { weatherRepository.getWeather(latitude, longitude) } returns sampleWeather()

        val result = useCase(taskId = 1, latitude = latitude, longitude = longitude)

        assertEquals(sampleTask(), result?.task)
        assertEquals(sampleWeather(), result?.weather)
    }

    @Test
    fun whenTaskDoesNotExist_invoke_shouldReturnNull() = runTest {
        coEvery { taskRepository.getTaskById(99) } returns null

        val result = useCase(taskId = 99, latitude = latitude, longitude = longitude)

        assertNull(result)
    }

    @Test
    fun whenTaskDoesNotExist_invoke_shouldNotFetchWeather() = runTest {
        coEvery { taskRepository.getTaskById(99) } returns null

        useCase(taskId = 99, latitude = latitude, longitude = longitude)

        coVerify(exactly = 0) { weatherRepository.getWeather(any(), any()) }
    }

    @Test
    fun whenWeatherRepositoryThrowsException_invoke_shouldPropagateException() = runTest {
        coEvery { taskRepository.getTaskById(1) } returns sampleTask()
        coEvery { weatherRepository.getWeather(latitude, longitude) } throws RuntimeException("network error")

        try {
            useCase(taskId = 1, latitude = latitude, longitude = longitude)
            fail("Expected an exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("network error", e.message)
        }
    }
}