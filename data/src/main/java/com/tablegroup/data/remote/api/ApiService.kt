package com.tablegroup.data.remote.api

import com.tablegroup.data.remote.dto.CityDto

interface ApiService  {
    suspend fun getCities(): List<CityDto>
}