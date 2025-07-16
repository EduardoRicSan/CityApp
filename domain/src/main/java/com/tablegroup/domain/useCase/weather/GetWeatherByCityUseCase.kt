package com.tablegroup.domain.useCase.weather

import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.domain.model.UIWeather
import com.tablegroup.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherByCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Flow<NetworkResult<UIWeather>> {
        return repository.getWeatherByCity(city)
    }
}