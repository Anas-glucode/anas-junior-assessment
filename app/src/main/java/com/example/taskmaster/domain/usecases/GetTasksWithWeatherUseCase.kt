package com.example.taskmaster.domain.usecases

import com.example.taskmaster.domain.models.TaskWithWeather
import com.example.taskmaster.domain.repos.TaskRepository
import com.example.taskmaster.domain.repos.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetTasksWithWeatherUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        taskId: Int,
        latitude: Double,
        longitude: Double
    ): Result<TaskWithWeather> = coroutineScope {
        runCatching {
            // Execute both repository calls concurrently
            val taskDeferred = async { taskRepository.getTaskById(taskId) }
            val weatherDeferred = async { weatherRepository.getWeather(latitude, longitude) }

            // Await both results
            val task = taskDeferred.await()
                ?: throw NoSuchElementException("Task with ID $taskId not found.")
            val weather = weatherDeferred.await()

            TaskWithWeather(task = task, weather = weather)
        }
    }
}