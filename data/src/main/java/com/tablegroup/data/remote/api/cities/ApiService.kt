package com.tablegroup.data.remote.api.cities

import com.tablegroup.data.remote.dto.CityDto

interface ApiService  {
    suspend fun getCities(): List<CityDto>
}