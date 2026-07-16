package com.example.taskmaster.data.remote.dto

import com.example.taskmaster.domain.models.Location
import com.example.taskmaster.domain.models.Weather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    @SerialName("location") val location: LocationDto,
    @SerialName("current") val current: CurrentDto
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
    @SerialName("temp_c") val tempC: Double
)


fun WeatherDto.toDomain(): Weather {
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
        sunrise = "--:--",
        sunset = "--:--"
    )
}