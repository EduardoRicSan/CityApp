package com.tablegroup.ualaapptest.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.domain.model.City
import com.tablegroup.domain.useCase.city.GetCitiesUseCase
import com.tablegroup.domain.useCase.city.GetCityByIdUseCase
import com.tablegroup.domain.useCase.city.GetFavoriteIdsUseCase
import com.tablegroup.domain.useCase.city.SyncCitiesUseCase
import com.tablegroup.domain.useCase.city.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel managing city data, filtering, and favorites.
 * Uses multiple use cases for syncing, fetching, toggling favorites, etc.
 */
@HiltViewModel
class CityViewModel @Inject constructor(
    private val syncCitiesUseCase: SyncCitiesUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val getCityByIdUseCase: GetCityByIdUseCase
) : ViewModel() {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    /** Search query state */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    /** State to filter favorites only */
    private val _onlyFavorites = MutableStateFlow(false)
    val onlyFavorites: StateFlow<Boolean> = _onlyFavorites.asStateFlow()

    /**
     * Combined state flow that filters cities by search query and favorites.
     * Emits Loading initially and updates when underlying data changes.
     */
    val filteredCities: StateFlow<NetworkResult<List<City>>> = combine(
        getCitiesUseCase(),
        getFavoriteIdsUseCase(),
        _searchQuery,
        _onlyFavorites
    ) { cityResult, favoriteIds, query, onlyFavs ->

        Log.d("CityViewModel", "CityResult: $cityResult")

        if (cityResult is NetworkResult.Success) {
            val filtered = cityResult.data.map { city ->
                city.copy(isFavorite = favoriteIds.contains(city.id))
            }.filter { city ->
                city.name.startsWith(query, ignoreCase = true) &&
                        (!onlyFavs || city.isFavorite)
            }.sortedWith(compareBy({ it.name.lowercase() }, { it.country.lowercase() }))

            NetworkResult.Success(filtered)
        } else {
            cityResult
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NetworkResult.Loading)

    init {
        viewModelScope.launch {
            _isSyncing.value = true
            syncCitiesUseCase()
            _isSyncing.value = false
        }
    }

    /** Update search query */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /** Toggle filter for only favorites */
    fun onToggleOnlyFavorites() {
        _onlyFavorites.value = !_onlyFavorites.value
    }

    /** Toggle a city's favorite status */
    fun toggleFavorite(cityId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase.toggleFavorite(cityId)
        }
    }

    /** Get city by ID, suspending function */
    suspend fun getCityById(cityId: Int): City? {
        return getCityByIdUseCase(cityId)
    }
}
