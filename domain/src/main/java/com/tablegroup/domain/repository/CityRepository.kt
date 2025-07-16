package com.tablegroup.domain.repository

import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.core.utils.remote.safeApiCall
import com.tablegroup.data.local.dataStore.CityDataStore
import com.tablegroup.data.local.room.dao.CityDao
import com.tablegroup.data.remote.api.cities.ApiService
import com.tablegroup.data.remote.dto.toEntity
import com.tablegroup.domain.model.City
import com.tablegroup.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository responsible for handling city-related data operations.
 * Combines API, local DB (Room), and preferences (DataStore) logic.
 */
class CityRepository @Inject constructor(
    private val api: ApiService,
    private val dao: CityDao,
    private val cityDataStore: CityDataStore
)  {

    /**
     * Synchronizes cities from API if local DB is empty.
     */
    fun syncCitiesIfNeeded(): Flow<NetworkResult<Unit>> = flow {
        val localCities = dao.getAllCities()
        if (localCities.isEmpty()) {
            safeApiCall { api.getCities() }.collect { remoteResult ->
                when (remoteResult) {
                    is NetworkResult.Success -> {
                        dao.insertCities(remoteResult.data.map { it.toEntity() })
                        emit(NetworkResult.Success(Unit))
                    }
                    is NetworkResult.Error -> {
                        emit(NetworkResult.Error(remoteResult.message))
                    }
                    else -> Unit
                }
            }
        } else {
            emit(NetworkResult.Success(Unit)) // Nothing to sync
        }
    }.flowOn(Dispatchers.IO)



    /**
     * Returns a flow of all cities from the DB, mapped to domain model and sorted.
     */
    fun getCities(): Flow<NetworkResult<List<City>>> = dao.getAllCitiesFlow()
        .map { entities ->
            val cities = entities.map { it.toDomain() }
                .sortedWith(compareBy({ it.name.lowercase() }, { it.country.lowercase() }))
            NetworkResult.Success(cities)
        }.flowOn(Dispatchers.IO)

    /**
     * Returns a flow of favorite city IDs from DataStore.
     */
    fun getFavoriteIdsFlow(): Flow<Set<Int>> = cityDataStore.getFavoriteIdsFlow()

    /**
     * Toggles favorite status for a given city ID in DataStore.
     */
    suspend fun toggleFavorite(cityId: Int) {
        cityDataStore.toggleFavorite(cityId)
    }

    /**
     * Returns a flow of favorite IDs (redundant to getFavoriteIdsFlow, consider removing one).
     */
    fun getFavoriteIds(): Flow<Set<Int>> = cityDataStore.getFavoriteIdsFlow()

    /**
     * Gets a city by ID from the database.
     */
    suspend fun getCityById(id: Int): City? {
        return dao.getCityById(id)?.toDomain()
    }
}
