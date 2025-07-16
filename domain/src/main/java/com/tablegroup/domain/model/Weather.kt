package com.tablegroup.domain.model

import com.tablegroup.data.remote.dto.WeatherResponseDTO

/**
 * UI model representing the weather information to be displayed on the screen.
 */
data class UIWeather(
    val city: String = "",
    val fullRegion: String = "",
    val localTime: String = "",
    val condition: String = "",
    val icon: String = "",
    val windKph: Double = 0.0,
    val humidity: Double = 0.0,
    val cloud: Double = 0.0,
)

/**
 * Maps WeatherResponseDTO (from API) to UIWeather model for UI representation.
 */
fun WeatherResponseDTO.toUIWeather() =
    UIWeather(
        city = this.location?.name.orEmpty(),
        fullRegion = "${this.location?.name}, ${this.location?.region}, ${this.location?.country}",
        localTime = this.location?.localtime.orEmpty(),
        condition = this.current?.condition?.text.orEmpty(),
        icon = this.current?.condition?.icon.orEmpty(),
        windKph =  this.current?.windKph ?: 0.0,
        humidity = this.current?.humidity ?: 0.0,
        cloud = this.current?.cloud ?: 0.0,
    )
