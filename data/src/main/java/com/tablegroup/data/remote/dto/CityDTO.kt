package com.tablegroup.data.remote.dto

import com.tablegroup.data.local.room.entities.CityEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CityDto(
    @SerialName("_id") val id: Int,
    val name: String,
    val country: String,
    val coord: CoordDto
)

@Serializable
data class CoordDto(
    val lon: Double,
    val lat: Double
)

fun CityDto.toEntity() = CityEntity(id, name, country, coord.lat, coord.lon)
