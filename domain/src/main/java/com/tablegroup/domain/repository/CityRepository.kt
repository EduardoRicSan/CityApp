package com.tablegroup.domain.repository

import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.core.utils.remote.safeApiCall
import com.tablegroup.data.local.dataStore.CityDataStore
import com.tablegroup.data.local.room.dao.CityDao
import com.tablegroup.data.remote.api.ApiService
import com.tablegroup.data.remote.dto.toEntity
import com.tablegroup.domain.model.City
import com.tablegroup.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val api: ApiService,
    private val dao: CityDao,
    private val cityDataStore: CityDataStore
)  {

    // Sincroniza la lista descargando e insertando si la DB está vacía
    suspend fun syncCitiesIfNeeded() = withContext(Dispatchers.IO) {
        val localCities = dao.getAllCities()
        if (localCities.isEmpty()) {
            val remoteResult = safeApiCall { api.getCities() }
            when (remoteResult) {
                is NetworkResult.Success -> {
                    dao.insertCities(remoteResult.data.map { it.toEntity() })
                }
                is NetworkResult.Error -> {
                    // log error si es necesario
                }
                else -> Unit
            }
        }
    }

    // Exponer flujo de ciudades desde Room
     fun getCities(): Flow<NetworkResult<List<City>>> = dao.getAllCitiesFlow()
        .map { entities ->
            val cities = entities.map { it.toDomain() }
                .sortedWith(compareBy({ it.name.lowercase() }, { it.country.lowercase() }))
            NetworkResult.Success(cities)
        }

    suspend fun toggleFavorite(cityId: Int) {
        cityDataStore.toggleFavorite(cityId)
    }

    fun getFavoriteIds(): Flow<Set<Int>> = cityDataStore.getFavoriteIdsFlow()

    suspend fun getCityById(id: Int): City? {
        return dao.getCityById(id)?.toDomain()
    }
}