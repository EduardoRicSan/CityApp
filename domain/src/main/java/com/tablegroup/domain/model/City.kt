package com.tablegroup.domain.model

import com.tablegroup.data.local.room.entities.CityEntity
import com.tablegroup.data.remote.dto.CityDto

data class City(
    val id: Int,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val isFavorite: Boolean = false
)

fun CityDto.toDomain(isFavorite: Boolean = false): City {
    return City(
        id = id,
        name = name,
        country = country,
        lat = coord.lat,
        lon = coord.lon,
        isFavorite = isFavorite
    )
}

fun CityEntity.toDomain() =
    City(id, name, country, lat, lon)