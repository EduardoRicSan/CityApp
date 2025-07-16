package com.tablegroup.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.tablegroup.core.utils.provider.ApiKeyProvider
import com.tablegroup.data.local.dataStore.CityDataStore
import com.tablegroup.data.local.room.AppDatabase
import com.tablegroup.data.local.room.dao.CityDao
import com.tablegroup.data.remote.api.cities.ApiConstants
import com.tablegroup.data.remote.api.cities.ApiService
import com.tablegroup.data.remote.api.cities.ApiServiceImpl
import com.tablegroup.data.remote.api.weather.WeatherApiConstants
import com.tablegroup.data.remote.api.weather.WeatherApiService
import com.tablegroup.data.remote.api.weather.WeatherApiServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    @CityClient
    fun provideCityHttpClient(): HttpClient = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
        defaultRequest {
            url(ApiConstants.BASE_URL)
            url {
                protocol = URLProtocol.HTTPS
            }
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    @Provides
    @Singleton
    @WeatherClient
    fun provideWeatherHttpClient(): HttpClient = HttpClient(Android) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
        defaultRequest {

            url {
                protocol = URLProtocol.HTTPS
                host = "api.weatherapi.com"
            }
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    @Provides
    @Singleton
    fun provideApiService(
        @CityClient client: HttpClient
    ): ApiService =
        ApiServiceImpl(client)

    @Provides
    @Singleton
    fun provideWeatherApiService(@WeatherClient client: HttpClient, apiKeyProvider: ApiKeyProvider): WeatherApiService =
        WeatherApiServiceImpl(client, apiKeyProvider)


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("app_preferences")
            }
        )
    }
    @Provides
    @Singleton
    fun provideAppPreferences(dataStore: DataStore<Preferences>): CityDataStore {
        return CityDataStore(dataStore)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideQuoteDao(database: AppDatabase): CityDao {
        return database.cityDao()
    }


}