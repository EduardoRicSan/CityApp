package com.tablegroup.domain.useCase.city

import com.tablegroup.core.utils.remote.NetworkResult
import com.tablegroup.domain.model.City
import com.tablegroup.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case responsible for retrieving the list of cities.
 * Delegates the operation to the CityRepository.
 *
 * @property repository The CityRepository instance used to fetch city data.
 */
class GetCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {

    /**
     * Invokes the use case to get the list of cities wrapped in NetworkResult.
     *
     * @return A Flow emitting the result of city retrieval.
     */
    operator fun invoke(): Flow<NetworkResult<List<City>>> {
        return repository.getCities()
    }
}
