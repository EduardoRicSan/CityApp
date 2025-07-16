package com.tablegroup.data.remote.api.weather

import com.tablegroup.data.remote.dto.WeatherResponseDTO

interface WeatherApiService  {
    suspend fun getWeatherByCity(city: String): WeatherResponseDTO
}