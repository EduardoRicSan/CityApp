package com.tablegroup.domain.useCase.city

import com.tablegroup.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to manage favorite cities.
 *
 * @property repository The CityRepository handling favorite city operations.
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: CityRepository
) {
    /**
     * Returns a Flow of favorite city IDs.
     */
    operator fun invoke(): Flow<Set<Int>> {
        return repository.getFavoriteIdsFlow()
    }

    /**
     * Toggles the favorite status for a city by its ID.
     *
     * @param cityId The ID of the city to toggle favorite.
     */
    suspend fun toggleFavorite(cityId: Int) {
        repository.toggleFavorite(cityId)
    }
}
