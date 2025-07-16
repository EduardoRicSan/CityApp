package com.tablegroup.domain.useCase.city

import com.tablegroup.domain.model.City
import com.tablegroup.domain.repository.CityRepository
import javax.inject.Inject

class GetCityByIdUseCase @Inject constructor(
    private val repository: CityRepository
) {
     suspend operator fun invoke(id: Int): City? {
        return repository.getCityById(id)
    }
}