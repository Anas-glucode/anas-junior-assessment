package com.example.taskmaster.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class Weather(
    val location: Location,
    val tempC: Double,
    val sunrise: String,
    val sunset: String
)