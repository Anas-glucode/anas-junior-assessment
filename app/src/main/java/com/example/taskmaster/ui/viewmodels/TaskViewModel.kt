package com.example.taskmaster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmaster.domain.models.Task
import com.example.taskmaster.domain.models.Weather
import com.example.taskmaster.domain.repos.TaskRepository
import com.example.taskmaster.domain.repos.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
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
import android.util.Log

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository, private val weatherRepository: WeatherRepository
) : ViewModel() {

    val tasks: Flow<List<Task>> = repository.getTask()

    private val _weather = MutableStateFlow<Weather?>(null)
    val weather: StateFlow<Weather?> = _weather.asStateFlow()

    private var weatherRefreshJob: Job? = null

    init {
        startWeatherRefreshLoop()
    }

    companion object {
        private const val WEATHER_REFRESH_INTERVAL_MILLIS = 60 * 60 * 1000L
        private const val AUTO_LOCATION = "auto:ip"
        private const val HILLBROW = "-26.1907,28.0473"
    }

    private fun startWeatherRefreshLoop() {
        weatherRefreshJob?.cancel()
        weatherRefreshJob = viewModelScope.launch {
            while (isActive) {
                fetchWeather()
                delay(WEATHER_REFRESH_INTERVAL_MILLIS.milliseconds)
            }
        }
    }

    private suspend fun fetchWeather() {
        _weather.value = try {
            weatherRepository.getWeather(AUTO_LOCATION)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("Weather", "auto:ip failed", e)
            try {
                weatherRepository.getWeather(HILLBROW)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("Weather", "Hillbrow fallback failed", e)
                null
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch { repository.insertTask(task) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { repository.deleteTask(task) }
    }
}