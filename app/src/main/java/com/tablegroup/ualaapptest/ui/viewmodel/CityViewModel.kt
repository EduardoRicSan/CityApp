package com.tablegroup.ualaapptest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.domain.model.City
import com.tablegroup.domain.useCase.GetCitiesUseCase
import com.tablegroup.domain.useCase.GetCityByIdUseCase
import com.tablegroup.domain.useCase.GetFavoriteIdsUseCase
import com.tablegroup.domain.useCase.SyncCitiesUseCase
import com.tablegroup.domain.useCase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val syncCitiesUseCase: SyncCitiesUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val getCityByIdUseCase: GetCityByIdUseCase
) : ViewModel() {

    // Estado para texto de búsqueda
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Estado para filtrar solo favoritos
    private val _onlyFavorites = MutableStateFlow(false)
    val onlyFavorites: StateFlow<Boolean> = _onlyFavorites.asStateFlow()

    // Flow que emite la lista combinada y filtrada
    val filteredCities: StateFlow<NetworkResult<List<City>>> = combine(
        getCitiesUseCase(),
        getFavoriteIdsUseCase(),
        _searchQuery,
        _onlyFavorites
    ) { cityResult, favoriteIds, query, onlyFavs ->

        if (cityResult is NetworkResult.Success) {
            val filtered = cityResult.data.filter { city ->
                city.name.startsWith(query, ignoreCase = true) &&
                        (!onlyFavs || favoriteIds.contains(city.id))
            }.sortedWith(compareBy({ it.name.lowercase() }, { it.country.lowercase() }))

            NetworkResult.Success(filtered)
        } else {
            // Pasa el estado Error o Loading tal cual
            cityResult
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NetworkResult.Loading())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            syncCitiesUseCase()
        }
    }

    // Funciones para actualizar estados
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onToggleOnlyFavorites() {
        _onlyFavorites.value = !_onlyFavorites.value
    }

    fun toggleFavorite(cityId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(cityId)
        }
    }

    suspend fun getCityById(cityId: Int): City? {
        return getCityByIdUseCase(cityId)
    }
}