package com.example.taskmaster.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val location: Location,
    val tempC: Double,
    val condition: String,
    val sunrise: String,
    val sunset: String
)