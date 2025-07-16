package com.tablegroup.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity representing a city.
 */
@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: Int,      // Unique identifier for the city
    val name: String,             // City name
    val country: String,          // Country name or code
    val lat: Double,              // Latitude coordinate
    val lon: Double               // Longitude coordinate
)