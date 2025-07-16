package com.tablegroup.ualaapptest.common.provider

import com.tablegroup.core.utils.provider.ApiKeyProvider
import javax.inject.Inject
import com.tablegroup.ualaapptest.BuildConfig

/**
 * Provides the weather API key from the BuildConfig.
 *
 * This implementation of [ApiKeyProvider] accesses the API key injected
 * at build time via Gradle, keeping sensitive data out of the source code.
 *
 * Annotated with @Inject to be used with Hilt dependency injection.
 */
class WeatherApiKeyProviderImpl @Inject constructor() : ApiKeyProvider {

    /**
     * Returns the weather API key from BuildConfig.
     */
    override fun getWeatherApiKey(): String = BuildConfig.WEATHER_API_KEY
}