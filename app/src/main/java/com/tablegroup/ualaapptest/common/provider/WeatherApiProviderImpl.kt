package com.tablegroup.ualaapptest.common.provider

import com.tablegroup.core.utils.provider.ApiKeyProvider
import javax.inject.Inject
import com.tablegroup.ualaapptest.BuildConfig

class WeatherApiKeyProviderImpl @Inject constructor() : ApiKeyProvider {
    override fun getWeatherApiKey(): String = BuildConfig.WEATHER_API_KEY
}