package com.example.taskmaster.data.remote.dto

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
    val condition: ConditionDto
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
    @SerialName("astro") val astro: AstroDto
)

@Serializable
data class AstroDto(
    @SerialName("sunrise") val sunrise: String,
    @SerialName("sunset") val sunset: String
)

fun WeatherDto.toDomain(): Weather {
    val astro = this.forecast?.forecastday?.firstOrNull()?.astro
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
        sunset = astro?.sunset ?: "--:--"
    )
}