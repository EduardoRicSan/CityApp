package com.tablegroup.data.local.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CityDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val FAVORITE_IDS_KEY = stringPreferencesKey("favorite_city_ids")
    }

    // Guarda la lista completa de favoritos como JSON
    suspend fun saveFavorites(ids: List<Int>) {
        val json = Json.encodeToString(ids)
        dataStore.edit { prefs ->
            prefs[FAVORITE_IDS_KEY] = json
        }
    }

    // Alterna un favorito (agrega o elimina el ID)
    suspend fun toggleFavorite(id: Int) {
        val currentFavorites = getFavoriteIdsOnce().toMutableSet()
        if (currentFavorites.contains(id)) {
            currentFavorites.remove(id)
        } else {
            currentFavorites.add(id)
        }
        saveFavorites(currentFavorites.toList())
    }

    // Flujo reactivo de IDs favoritos
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

    // Obtener favoritos una sola vez (suspend)
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