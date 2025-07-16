package com.tablegroup.domain.useCase.city

import com.tablegroup.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteIdsUseCase @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<Set<Int>> {
        return repository.getFavoriteIds()
    }
}