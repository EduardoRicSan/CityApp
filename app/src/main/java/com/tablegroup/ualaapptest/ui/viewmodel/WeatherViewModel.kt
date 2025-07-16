package com.tablegroup.ualaapptest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.domain.model.UIWeather
import com.tablegroup.domain.useCase.weather.GetWeatherByCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for fetching weather data by city.
 * Exposes a StateFlow of NetworkResult to represent loading, success, and error states.
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherByCityUseCase: GetWeatherByCityUseCase,
): ViewModel() {

    // Holds the current weather data or status
    private val _weatherCity = MutableStateFlow<NetworkResult<UIWeather>>(NetworkResult.Loading)
    val weatherCity: StateFlow<NetworkResult<UIWeather>> = _weatherCity

    /**
     * Fetches weather info for a city and updates the state flow accordingly.
     * Runs in IO dispatcher.
     */
    fun getWeatherByCity(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherByCityUseCase.invoke(city).collect {
                _weatherCity.value = it
            }
        }
    }
}
