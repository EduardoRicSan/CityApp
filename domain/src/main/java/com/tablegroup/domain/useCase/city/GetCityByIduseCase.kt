package com.tablegroup.domain.useCase.city

import com.tablegroup.domain.model.City
import com.tablegroup.domain.repository.CityRepository
import javax.inject.Inject

/**
 * Use case for retrieving a single city by its ID.
 *
 * @property repository The CityRepository that provides access to city data.
 */
class GetCityByIdUseCase @Inject constructor(
    private val repository: CityRepository
) {

    /**
     * Retrieves a city by its unique ID.
     *
     * @param id The ID of the city to retrieve.
     * @return The matching City object or null if not found.
     */
    suspend operator fun invoke(id: Int): City? {
        return repository.getCityById(id)
    }
}
