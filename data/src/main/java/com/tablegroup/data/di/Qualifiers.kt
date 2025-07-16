package com.tablegroup.data.di

import jakarta.inject.Qualifier

/**
 * Qualifier annotation to distinguish Weather API HttpClient.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherClient

/**
 * Qualifier annotation to distinguish City API HttpClient.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CityClient
