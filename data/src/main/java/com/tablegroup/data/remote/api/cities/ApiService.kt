package com.tablegroup.data.remote.api.cities

import com.tablegroup.data.remote.dto.CityDto

/**
 * Defines the API contract for fetching city-related data.
 */
interface ApiService {
    /**
     * Fetches the list of cities from the remote source.
     * @return List of CityDto objects representing cities.
     */
    suspend fun getCities(): List<CityDto>
}
