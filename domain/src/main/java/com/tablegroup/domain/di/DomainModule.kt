package com.tablegroup.domain.di

import com.tablegroup.data.local.dataStore.CityDataStore
import com.tablegroup.data.local.room.dao.CityDao
import com.tablegroup.data.remote.api.cities.ApiService
import com.tablegroup.data.remote.api.weather.WeatherApiService
import com.tablegroup.domain.repository.CityRepository
import com.tablegroup.domain.repository.WeatherRepository
import com.tablegroup.domain.useCase.city.GetCitiesUseCase
import com.tablegroup.domain.useCase.city.GetCityByIdUseCase
import com.tablegroup.domain.useCase.city.GetFavoriteIdsUseCase
import com.tablegroup.domain.useCase.city.SyncCitiesUseCase
import com.tablegroup.domain.useCase.city.ToggleFavoriteUseCase
import com.tablegroup.domain.useCase.weather.GetWeatherByCityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetCitiesUseCase(
        cityRepository: CityRepository
    ): GetCitiesUseCase = GetCitiesUseCase(cityRepository)


    @Provides
    @Singleton
    fun provideGetCityByIdUseCase(
        cityRepository: CityRepository
    ): GetCityByIdUseCase = GetCityByIdUseCase(cityRepository)

    @Provides
    @Singleton
    fun provideGetFavoriteIdsUseCase(
        cityRepository: CityRepository
    ): GetFavoriteIdsUseCase = GetFavoriteIdsUseCase(cityRepository)

    @Provides
    @Singleton
    fun provideToogleFavoriteUseCase(
        cityRepository: CityRepository
    ): ToggleFavoriteUseCase = ToggleFavoriteUseCase(cityRepository)

    @Provides
    @Singleton
    fun provideSyncCitiesUseCase(
        cityRepository: CityRepository
    ): SyncCitiesUseCase = SyncCitiesUseCase(cityRepository)


    @Provides
    @Singleton
    fun provideCityRepository(
        api: ApiService,
        dao: CityDao,
        cityDataStore: CityDataStore,
    ): CityRepository = CityRepository(api, dao, cityDataStore)

    //WEATHER
    @Provides
    @Singleton
    fun provideGetWeatherByCityUseCase(
        weatherRepository: WeatherRepository
    ): GetWeatherByCityUseCase = GetWeatherByCityUseCase(weatherRepository)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApiService,
    ): WeatherRepository = WeatherRepository(api)


}