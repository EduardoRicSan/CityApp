package com.tablegroup.domain.useCase.city

import com.tablegroup.domain.repository.CityRepository
import javax.inject.Inject

class SyncCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {
     suspend operator fun invoke() {
        repository.syncCitiesIfNeeded()
    }
}