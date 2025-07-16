package com.tablegroup.domain.useCase.city

import com.tablegroup.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<Set<Int>> {
        return repository.getFavoriteIdsFlow()
    }

     suspend fun toggleFavorite(cityId: Int) {
        repository.toggleFavorite(cityId)
    }
}