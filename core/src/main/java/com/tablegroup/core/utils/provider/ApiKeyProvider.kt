package com.tablegroup.core.utils.provider

/**
 * Interface to provide API keys required for network requests.
 * Currently provides the API key for the weather service.
 */
interface ApiKeyProvider {
    fun getWeatherApiKey(): String
}

