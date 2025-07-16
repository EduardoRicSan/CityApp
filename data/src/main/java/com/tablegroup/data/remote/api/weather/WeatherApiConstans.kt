package com.tablegroup.data.remote.api.weather

object WeatherApiConstants {
    const val BASE_HOST = "api.weatherapi.com"
    const val GET_WEATHER = "v1/current.json"
}

object WeatherApiParams {
    const val API_KEY_PARAM = "key"
    const val CITY_PARAM = "q"
    const val AQI_PARAM = "aqi"
    const val AQI_PARAM_VALUE = "no"
}