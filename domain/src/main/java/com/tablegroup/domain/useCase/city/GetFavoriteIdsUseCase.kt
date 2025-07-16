package com.tablegroup.domain.useCase.city

import com.tablegroup.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the flow of favorite city IDs.
 *
 * @property repository The CityRepository providing access to favorite IDs.
 */
class GetFavoriteIdsUseCase @Inject constructor(
    private val repository: CityRepository
) {

    /**
     * Returns a Flow emitting sets of favorite city IDs.
     *
     * @return Flow of Set of city IDs marked as favorites.
     */
    operator fun invoke(): Flow<Set<Int>> {
        return repository.getFavoriteIds()
    }
}
