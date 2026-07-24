package com.example.taskmaster.domain.usecases

import javax.inject.Inject
import com.example.taskmaster.domain.models.TaskWithWeather
import com.example.taskmaster.domain.repos.TaskRepository
import com.example.taskmaster.domain.repos.WeatherRepository

class GetTasksWithWeatherUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        taskId: Int,
        latitude: Double,
        longitude: Double
    ): TaskWithWeather? {

        val task = taskRepository.getTaskById(taskId) ?: return null

        val weather = weatherRepository.getWeather(latitude, longitude)

        return TaskWithWeather(task = task, weather = weather)
    }
}