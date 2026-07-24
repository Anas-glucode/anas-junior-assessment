package com.example.taskmaster

import com.example.taskmaster.data.remote.dto.AstroDto
import com.example.taskmaster.data.remote.dto.ConditionDto
import com.example.taskmaster.data.remote.dto.CurrentDto
import com.example.taskmaster.data.remote.dto.ForecastDayDto
import com.example.taskmaster.data.remote.dto.ForecastDto
import com.example.taskmaster.data.remote.dto.LocationDto
import com.example.taskmaster.data.remote.dto.WeatherDto
import com.example.taskmaster.data.remote.dto.toDomain
import com.example.taskmaster.domain.models.Location
import com.example.taskmaster.domain.models.Weather
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherDtoMappingTest {

    private fun sampleLocationDto() = LocationDto(
        name = "Johannesburg",
        region = "Gauteng",
        country = "South Africa",
        lat = -26.2041,
        lon = 28.0473,
        localtimeEpoch = 1_753_000_000L,
        localtime = "2026-07-24 10:00"
    )

    private fun sampleCurrentDto(
        tempC: Double = 18.5,
        conditionText: String = "Partly cloudy"
    ) = CurrentDto(
        tempC = tempC,
        condition = ConditionDto(text = conditionText, icon = "//cdn.weatherapi.com/icon.png")
    )

    @Test
    fun whenForecastAndAstroArePresent_toDomain_shouldMapAllFields() {
        val dto = WeatherDto(
            location = sampleLocationDto(),
            current = sampleCurrentDto(),
            forecast = ForecastDto(
                forecastday = listOf(
                    ForecastDayDto(astro = AstroDto(sunrise = "06:12 AM", sunset = "05:32 PM"))
                )
            )
        )

        val result = dto.toDomain()

        assertEquals(
            Weather(
                location = Location(
                    name = "Johannesburg",
                    region = "Gauteng",
                    country = "South Africa",
                    lat = -26.2041,
                    lon = 28.0473,
                    localtimeEpoch = 1_753_000_000L,
                    localtime = "2026-07-24 10:00"
                ),
                tempC = 18.5,
                condition = "Partly cloudy",
                sunrise = "06:12 AM",
                sunset = "05:32 PM"
            ),
            result
        )
    }

    @Test
    fun whenForecastIsNull_toDomain_shouldReturnPlaceholderSunTimes() {
        val dto = WeatherDto(
            location = sampleLocationDto(),
            current = sampleCurrentDto(),
            forecast = null
        )

        val result = dto.toDomain()

        assertEquals("--:--", result.sunrise)
        assertEquals("--:--", result.sunset)
    }

    @Test
    fun whenForecastDayListIsEmpty_toDomain_shouldReturnPlaceholderSunTimes() {
        val dto = WeatherDto(
            location = sampleLocationDto(),
            current = sampleCurrentDto(),
            forecast = ForecastDto(forecastday = emptyList())
        )

        val result = dto.toDomain()

        assertEquals("--:--", result.sunrise)
        assertEquals("--:--", result.sunset)
    }

    @Test
    fun whenTemperatureIsNegative_toDomain_shouldMapTempAndConditionCorrectly() {
        val dto = WeatherDto(
            location = sampleLocationDto(),
            current = sampleCurrentDto(tempC = -3.2, conditionText = "Light snow"),
            forecast = null
        )

        val result = dto.toDomain()

        assertEquals(-3.2, result.tempC, 0.0001)
        assertEquals("Light snow", result.condition)
    }
}