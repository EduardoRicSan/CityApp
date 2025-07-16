package com.tablegroup.ualaapptest.di

import com.tablegroup.core.utils.provider.ApiKeyProvider
import com.tablegroup.ualaapptest.common.provider.WeatherApiKeyProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiKeyProvider(): ApiKeyProvider = WeatherApiKeyProviderImpl()

}