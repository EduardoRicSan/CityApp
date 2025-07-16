package com.tablegroup.domain.model

import com.tablegroup.data.local.room.entities.CityEntity
import kotlinx.serialization.Serializable

/**
 * Domain model representing a City.
 * Annotated with @Serializable to support Kotlin Serialization.
 */
@Serializable
data class City(
    val id: Int,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val isFavorite: Boolean = false
)

/**
 * Converts a CityEntity (from database) into a City domain model.
 */
fun CityEntity.toDomain() =
    City(id, name, country, lat, lon)

/**
 * Converts a nullable City object into a map of its properties.
 * Used for saving/restoring state with rememberSaveable.
 */
fun City?.toMap(): Map<String, Any> =
    this?.let {
        mapOf(
            "id" to it.id,
            "name" to it.name,
            "country" to it.country,
            "lat" to it.lat,
            "lon" to it.lon,
            "isFavorite" to it.isFavorite
        )
    } ?: emptyMap()

/**
 * Restores a City object from a map of properties.
 * Used together with toMap() for state restoration.
 */
fun Map<String, Any>.toCity(): City? =
    if (this.isEmpty()) null else City(
        id = this["id"] as Int,
        name = this["name"] as String,
        country = this["country"] as String,
        lat = this["lat"] as Double,
        lon = this["lon"] as Double,
        isFavorite = this["isFavorite"] as Boolean
    )

