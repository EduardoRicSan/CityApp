package com.tablegroup.ualaapptest.di

import com.tablegroup.core.utils.provider.ApiKeyProvider
import com.tablegroup.ualaapptest.common.provider.WeatherApiKeyProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides application-level dependencies.
 *
 * This module is installed in the [SingletonComponent], making all provided
 * dependencies available as singletons throughout the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a singleton instance of [ApiKeyProvider].
     *
     * Uses [WeatherApiKeyProviderImpl] to access API keys from BuildConfig.
     */
    @Provides
    @Singleton
    fun provideApiKeyProvider(): ApiKeyProvider = WeatherApiKeyProviderImpl()
}