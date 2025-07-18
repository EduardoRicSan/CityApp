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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel managing city data, filtering, and favorites.
 * Uses multiple use cases for syncing, fetching, toggling favorites, etc.
 */
const val DEBOUNCE_TIME_OUT = 300L
@OptIn(FlowPreview::class)
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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _onlyFavorites = MutableStateFlow(false)
    val onlyFavorites: StateFlow<Boolean> = _onlyFavorites.asStateFlow()

    private val debouncedQuery = _searchQuery
        .debounce(DEBOUNCE_TIME_OUT)
        .distinctUntilChanged()

    val filteredCities: StateFlow<NetworkResult<List<City>>> = combine(
        getCitiesUseCase(),
        getFavoriteIdsUseCase(),
        debouncedQuery,
        _onlyFavorites
    ) { cityResult, favoriteIds, query, onlyFavs ->

        if (cityResult is NetworkResult.Success) {
            val filtered = cityResult.data.map { city ->
                city.copy(isFavorite = favoriteIds.contains(city.id))
            }.filter { city ->
                city.name.contains(query, ignoreCase = true) &&
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

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onToggleOnlyFavorites() {
        _onlyFavorites.value = !_onlyFavorites.value
    }

    fun toggleFavorite(cityId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase.toggleFavorite(cityId)
        }
    }

    suspend fun getCityById(cityId: Int): City? {
        return getCityByIdUseCase(cityId)
    }
}

