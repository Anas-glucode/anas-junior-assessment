package com.example.taskmaster.data.remote.dto

import com.example.taskmaster.domain.models.DailyForecast
import com.example.taskmaster.domain.models.Location
import com.example.taskmaster.domain.models.Weather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    @SerialName("location") val location: LocationDto,
    @SerialName("current") val current: CurrentDto,
    @SerialName("forecast") val forecast: ForecastDto? = null
)

@Serializable
data class LocationDto(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerialName("localtime_epoch") val localtimeEpoch: Long,
    val localtime: String
)

@Serializable
data class CurrentDto(
    @SerialName("temp_c") val tempC: Double,
    val condition: ConditionDto,
    val humidity: Int = 0,
    val uv: Double = 0.0,
    @SerialName("wind_kph") val windKph: Double = 0.0,
    @SerialName("wind_degree") val windDegree: Int = 0,
    @SerialName("wind_dir") val windDir: String = "",
    @SerialName("gust_kph") val gustKph: Double = 0.0
)

@Serializable
data class ConditionDto(
    @SerialName("text") val text: String,
    @SerialName("icon") val icon: String
)

@Serializable
data class ForecastDto(
    @SerialName("forecastday") val forecastday: List<ForecastDayDto> = emptyList()
)

@Serializable
data class ForecastDayDto(
    val date: String = "",
    val day: DayDto = DayDto(),
    @SerialName("astro") val astro: AstroDto
)

@Serializable
data class DayDto(
    @SerialName("maxtemp_c") val maxTempC: Double = 0.0,
    @SerialName("mintemp_c") val minTempC: Double = 0.0,
    @SerialName("daily_chance_of_rain") val dailyChanceOfRain: Int = 0,
    val condition: ConditionDto = ConditionDto(text = "", icon = "")
)

@Serializable
data class AstroDto(
    @SerialName("sunrise") val sunrise: String,
    @SerialName("sunset") val sunset: String
)

fun WeatherDto.toDomain(): Weather {
    val days = this.forecast?.forecastday.orEmpty()
    val astro = days.firstOrNull()?.astro
    return Weather(
        location = Location(
            name = this.location.name,
            region = this.location.region,
            country = this.location.country,
            lat = this.location.lat,
            lon = this.location.lon,
            localtimeEpoch = this.location.localtimeEpoch,
            localtime = this.location.localtime
        ),
        tempC = this.current.tempC,
        condition = this.current.condition.text,
        sunrise = astro?.sunrise ?: "--:--",
        sunset = astro?.sunset ?: "--:--",
        humidity = this.current.humidity,
        uvIndex = this.current.uv,
        windKph = this.current.windKph,
        windDegree = this.current.windDegree,
        windDir = this.current.windDir,
        gustKph = this.current.gustKph,
        daily = days.map {
            DailyForecast(
                date = it.date,
                maxTempC = it.day.maxTempC,
                minTempC = it.day.minTempC,
                condition = it.day.condition.text,
                chanceOfRain = it.day.dailyChanceOfRain
            )
        }
    )
}