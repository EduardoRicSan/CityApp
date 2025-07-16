package com.tablegroup.data.remote.dto

import com.tablegroup.data.local.room.entities.CityEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Data Transfer Object representing a City from the API response.
 *
 * @property id Unique city identifier (mapped from "_id").
 * @property name Name of the city.
 * @property country Country code of the city.
 * @property coord Coordinates of the city.
 */
@Serializable
data class CityDto(
    @SerialName("_id") val id: Int,
    val name: String,
    val country: String,
    val coord: CoordDto
)

/**
 * Data Transfer Object representing geographical coordinates.
 *
 * @property lon Longitude value.
 * @property lat Latitude value.
 */
@Serializable
data class CoordDto(
    val lon: Double,
    val lat: Double
)

/**
 * Extension function to map CityDto to CityEntity (database model).
 *
 * @return CityEntity instance for Room database.
 */
fun CityDto.toEntity() = CityEntity(id, name, country, coord.lat, coord.lon)

