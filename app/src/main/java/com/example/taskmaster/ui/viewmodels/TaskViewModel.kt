package com.example.taskmaster.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmaster.domain.models.Task
import com.example.taskmaster.domain.models.Weather
import com.example.taskmaster.domain.repos.TaskRepository
import com.example.taskmaster.domain.repos.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    val tasks: Flow<List<Task>> = repository.getTask()

    private val _weather = MutableStateFlow<Weather?>(null)
    val weather: StateFlow<Weather?> = _weather.asStateFlow()

    init {
        viewModelScope.launch {
            while (isActive) {
                _weather.value = fetchWeather(AUTO_LOCATION).getOrElse {
                    fetchWeather(HILLBROW).getOrNull()
                }
                delay(WEATHER_REFRESH_INTERVAL_MILLIS.milliseconds)
            }
        }
    }

    private suspend fun fetchWeather(location: String): Result<Weather?> = try {
        Result.success(weatherRepository.getWeather(location))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.e("Weather", "$location failed", e)
        Result.failure(e)
    }

    fun addTask(task: Task) {
        viewModelScope.launch { repository.insertTask(task) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { repository.deleteTask(task) }
    }

    private companion object {
        const val WEATHER_REFRESH_INTERVAL_MILLIS = 60 * 60 * 1000L
        const val AUTO_LOCATION = "auto:ip"
        const val HILLBROW = "-26.1907,28.0473"
    }
}