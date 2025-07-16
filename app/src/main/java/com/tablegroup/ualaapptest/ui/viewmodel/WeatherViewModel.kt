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

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherByCityUseCase: GetWeatherByCityUseCase,
): ViewModel() {

    private val _weatherCity = MutableStateFlow<NetworkResult<UIWeather>>(NetworkResult.Loading)
    val weatherCity: StateFlow<NetworkResult<UIWeather>> = _weatherCity

    fun getWeatherByCity(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherByCityUseCase.invoke(city).collect {
                _weatherCity.value = it
            }
        }
    }
}