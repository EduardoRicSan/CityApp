package com.tablegroup.domain.useCase.weather

import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.domain.model.UIWeather
import com.tablegroup.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to fetch weather information for a given city.
 *
 * @property repository The WeatherRepository to retrieve weather data.
 */
class GetWeatherByCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    /**
     * Invokes the use case to get weather info as a Flow of NetworkResult.
     *
     * @param city The city name to fetch weather for.
     * @return Flow emitting NetworkResult of UIWeather data.
     */
    suspend operator fun invoke(city: String): Flow<NetworkResult<UIWeather>> {
        return repository.getWeatherByCity(city)
    }
}
