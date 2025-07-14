package com.tablegroup.domain.useCase

import com.tablegroup.domain.repository.CityRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke(cityId: Int) {
        repository.toggleFavorite(cityId)
    }
}