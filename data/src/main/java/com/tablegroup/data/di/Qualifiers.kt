package com.tablegroup.data.di

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CityClient