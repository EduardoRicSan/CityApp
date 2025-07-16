package com.tablegroup.data.remote.api.weather

import com.tablegroup.data.remote.dto.WeatherResponseDTO

/**
 * Interface defining Weather API operations.
 */
interface WeatherApiService {
    /**
     * Fetches current weather data for the given city.
     *
     * @param city Name of the city to fetch weather for.
     * @return WeatherResponseDTO containing weather details.
     */
    suspend fun getWeatherByCity(city: String): WeatherResponseDTO
}
