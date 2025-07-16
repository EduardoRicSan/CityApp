package com.tablegroup.data.local.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Wrapper class for managing favorite city IDs using DataStore.
 */
class CityDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val FAVORITE_IDS_KEY = stringPreferencesKey("favorite_city_ids")
    }

    /**
     * Saves the list of favorite city IDs as a JSON string in DataStore.
     */
    suspend fun saveFavorites(ids: List<Int>) {
        val json = Json.encodeToString(ids)
        dataStore.edit { prefs ->
            prefs[FAVORITE_IDS_KEY] = json
        }
    }

    /**
     * Toggles a city ID in the favorites list.
     * Adds it if not present; removes it if already favorited.
     */
    suspend fun toggleFavorite(id: Int) {
        val currentFavorites = getFavoriteIdsOnce().toMutableSet()
        if (currentFavorites.contains(id)) {
            currentFavorites.remove(id)
        } else {
            currentFavorites.add(id)
        }
        saveFavorites(currentFavorites.toList())
    }

    /**
     * Returns a Flow emitting the current set of favorite city IDs.
     * Uses JSON decoding and handles errors gracefully.
     */
    fun getFavoriteIdsFlow(): Flow<Set<Int>> {
        return dataStore.data
            .map { prefs ->
                prefs[FAVORITE_IDS_KEY]?.let {
                    try {
                        Json.decodeFromString<List<Int>>(it).toSet()
                    } catch (e: Exception) {
                        emptySet()
                    }
                } ?: emptySet()
            }
            .distinctUntilChanged()
    }

    /**
     * Returns the current set of favorite city IDs, read once.
     * Suspends until the first data is available.
     */
    suspend fun getFavoriteIdsOnce(): Set<Int> {
        val prefs = dataStore.data.first()
        return prefs[FAVORITE_IDS_KEY]?.let {
            try {
                Json.decodeFromString<List<Int>>(it).toSet()
            } catch (e: Exception) {
                emptySet()
            }
        } ?: emptySet()
    }
}
