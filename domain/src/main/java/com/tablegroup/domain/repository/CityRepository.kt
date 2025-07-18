package com.tablegroup.domain.repository

import android.util.Log
import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.core.utils.remote.safeApiCall
import com.tablegroup.data.local.dataStore.CityDataStore
import com.tablegroup.data.local.room.dao.CityDao
import com.tablegroup.data.local.room.entities.CityEntity
import com.tablegroup.data.remote.api.cities.ApiService
import com.tablegroup.data.remote.dto.toEntity
import com.tablegroup.domain.model.City
import com.tablegroup.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
        try {
            val localCities = dao.getAllCities()
            Log.d("CityRepository", "Local DB cities count before sync: ${localCities.size}")

            if (localCities.isEmpty()) {
                Log.d("CityRepository", "Starting cities sync from remote API...")
                safeApiCall { api.getCities() }.collect { remoteResult ->
                    when (remoteResult) {
                        is NetworkResult.Success -> {
                            Log.d("CityRepository", "Fetched remote cities count: ${remoteResult.data.size}")
                            // Inserción en base de datos
                            insertCitiesInBatches(remoteResult.data.map { it.toEntity() })
                            Log.d("CityRepository", "Inserted cities into DB successfully")
                            emit(NetworkResult.Success(Unit))
                        }
                        is NetworkResult.Error -> {
                            Log.e("CityRepository", "Error fetching cities: ${remoteResult.message}")
                            emit(NetworkResult.Error(remoteResult.message))
                        }
                        else -> {
                            Log.w("CityRepository", "Unexpected NetworkResult state during sync")
                        }
                    }
                }
            } else {
                Log.d("CityRepository", "Local DB already has cities, skipping sync.")
                emit(NetworkResult.Success(Unit))
            }
        } catch (e: Exception) {
            Log.e("CityRepository", "Exception during syncCitiesIfNeeded: ${e.message}", e)
            emit(NetworkResult.Error("Exception: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun insertCitiesInBatches(cities: List<CityEntity>, batchSize: Int = 5000) {
        for (chunk in cities.chunked(batchSize)) {
            dao.insertCities(chunk)
            Log.d("CityRepository", "Inserted batch of ${chunk.size} cities")
        }
    }

    /**
     * Returns a flow of all cities from the DB, mapped to domain model and sorted.
     */
    fun getCities(): Flow<NetworkResult<List<City>>> = dao.getAllCitiesFlow()
        .distinctUntilChanged()
        .map { entities ->
            NetworkResult.Success(entities.map { it.toDomain() })
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
