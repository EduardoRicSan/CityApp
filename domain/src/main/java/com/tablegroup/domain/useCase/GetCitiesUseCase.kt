package com.tablegroup.domain.useCase

import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.domain.model.City
import com.tablegroup.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<NetworkResult<List<City>>> {
        return repository.getCities()
    }
}