package com.tablegroup.domain.useCase.city

import com.tablegroup.domain.repository.CityRepository
import javax.inject.Inject

/**
 * Use case to synchronize cities data if needed.
 *
 * @property repository The CityRepository responsible for syncing city data.
 */
class SyncCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {
    /**
     * Invokes the synchronization process.
     */
    suspend operator fun invoke() {
        repository.syncCitiesIfNeeded()
    }
}
