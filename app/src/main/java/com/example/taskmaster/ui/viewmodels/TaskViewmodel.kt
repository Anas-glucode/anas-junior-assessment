package com.example.taskmaster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmaster.domain.models.Task
import com.example.taskmaster.domain.models.Weather
import com.example.taskmaster.domain.repos.TaskRepository
import com.example.taskmaster.domain.repos.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private var weatherRefreshJob: Job? = null

    init {
        startWeatherRefreshLoop(-26.2041, 28.0473)
    }

    companion object {
        private const val WEATHER_REFRESH_INTERVAL_MILLIS = 60 * 60 * 1000L
    }

    fun startWeatherRefreshLoop(latitude: Double, longitude: Double) {
        weatherRefreshJob?.cancel()
        weatherRefreshJob = viewModelScope.launch {
            while (isActive) {
                fetchWeather(latitude, longitude)
                delay(WEATHER_REFRESH_INTERVAL_MILLIS.milliseconds)
            }
        }
    }

    fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                _weather.value = weatherRepository.getWeather(latitude, longitude)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}