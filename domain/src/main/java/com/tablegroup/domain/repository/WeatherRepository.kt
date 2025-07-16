package com.tablegroup.domain.repository

import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.core.utils.remote.safeApiCall
import com.tablegroup.data.remote.api.weather.WeatherApiService
import com.tablegroup.domain.model.UIWeather
import com.tablegroup.domain.model.toUIWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


/**
 * Repository responsible for fetching weather data from the Weather API.
 * Converts the API response to a UI-friendly model and wraps it in a NetworkResult.
 */
class WeatherRepository @Inject constructor(
    private val api: WeatherApiService,
) {

    /**
     * Retrieves weather data for the specified city.
     * Uses a safe API call wrapper to emit loading, success, or error states.
     *
     * @param city Name of the city to fetch weather for.
     * @return A Flow emitting NetworkResult<UIWeather>.
     */
    suspend fun getWeatherByCity(city: String): Flow<NetworkResult<UIWeather>> = safeApiCall {
        api.getWeatherByCity(city).toUIWeather()
    }.flowOn(Dispatchers.IO)
}
