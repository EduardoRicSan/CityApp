package com.tablegroup.data.remote.api.weather

import com.tablegroup.core.utils.provider.ApiKeyProvider
import com.tablegroup.data.remote.dto.WeatherResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Implementation of WeatherApiService using Ktor HttpClient.
 *
 * @property weatherClient Configured HttpClient for weather API requests.
 * @property apiKeyProvider Provides the API key for authentication.
 */
class WeatherApiServiceImpl(
    private val weatherClient: HttpClient,
    private val apiKeyProvider: ApiKeyProvider
) : WeatherApiService {

    /**
     * Fetches weather data for a specified city by calling the weather API.
     *
     * @param city City name to fetch weather for.
     * @return WeatherResponseDTO containing the weather information.
     */
    override suspend fun getWeatherByCity(city: String): WeatherResponseDTO {
        val apiKey = apiKeyProvider.getWeatherApiKey()
        val response: WeatherResponseDTO =
            weatherClient.get(WeatherApiConstants.GET_WEATHER) {
                parameter(WeatherApiParams.API_KEY_PARAM, apiKey)
                parameter(WeatherApiParams.CITY_PARAM, city)
                parameter(WeatherApiParams.AQI_PARAM, WeatherApiParams.AQI_PARAM_VALUE)
            }.body()
        return response
    }
}
