package com.tablegroup.data.remote.api.weather

import com.tablegroup.core.utils.provider.ApiKeyProvider
import com.tablegroup.data.remote.dto.WeatherResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WeatherApiServiceImpl(
    private val weatherClient: HttpClient,
    private val apiKeyProvider: ApiKeyProvider
) : WeatherApiService {

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