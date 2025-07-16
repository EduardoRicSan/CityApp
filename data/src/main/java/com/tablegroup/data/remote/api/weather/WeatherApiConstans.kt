package com.tablegroup.data.remote.api.weather

/**
 * Constants related to the Weather API endpoints and parameters.
 */
object WeatherApiConstants {
    const val BASE_HOST = "api.weatherapi.com"      // Base host for weather API
    const val GET_WEATHER = "v1/current.json"       // Endpoint path for current weather data
}

/**
 * Query parameter keys and default values for Weather API requests.
 */
object WeatherApiParams {
    const val API_KEY_PARAM = "key"                  // API key query parameter name
    const val CITY_PARAM = "q"                        // City query parameter name
    const val AQI_PARAM = "aqi"                       // Air Quality Index query parameter name
    const val AQI_PARAM_VALUE = "no"                  // Default value to disable AQI data in response
}
