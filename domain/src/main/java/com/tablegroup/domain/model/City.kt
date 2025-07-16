package com.tablegroup.domain.model

import android.os.Parcelable
import com.tablegroup.data.local.room.entities.CityEntity
import com.tablegroup.data.remote.dto.CityDto
import kotlinx.serialization.Serializable

@Serializable
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

fun Map<String, Any>.toCity(): City? =
    if (this.isEmpty()) null else City(
        id = this["id"] as Int,
        name = this["name"] as String,
        country = this["country"] as String,
        lat = this["lat"] as Double,
        lon = this["lon"] as Double,
        isFavorite = this["isFavorite"] as Boolean
    )
