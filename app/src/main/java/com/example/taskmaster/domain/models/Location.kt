package com.example.taskmaster.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val localtimeEpoch: Long,
    val localtime: String

)
