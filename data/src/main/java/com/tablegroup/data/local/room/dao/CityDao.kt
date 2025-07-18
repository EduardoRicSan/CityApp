package com.tablegroup.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tablegroup.data.local.room.entities.CityEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO interface for accessing CityEntity data from the local Room database.
 */
@Dao
interface CityDao {

    /**
     * Returns all cities sorted by name and country (case insensitive).
     * This is a suspend function for one-time queries.
     */
    @Query("SELECT * FROM cities ORDER BY LOWER(name), LOWER(country)")
    suspend fun getAllCities(): List<CityEntity>

    /**
     * Returns a Flow that emits the list of all cities from the database,
     * sorted alphabetically by name and country in a case-insensitive manner.
     *
     * The SQL clause `COLLATE NOCASE` specifies that the sorting
     * should ignore case differences (e.g., "Amsterdam" and "amsterdam"
     * are treated as equal for ordering purposes).
     *
     * This Flow will emit updated lists automatically whenever the underlying
     * database data changes, enabling reactive UI updates.
     */
    @Query("SELECT * FROM cities ORDER BY name COLLATE NOCASE ASC, country COLLATE NOCASE ASC")
    fun getAllCitiesFlow(): Flow<List<CityEntity>>

    /**
     * Inserts or updates a list of cities in the database.
     * Conflicts are resolved by replacing existing entries.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    /**
     * Retrieves a city entity by its ID, or null if not found.
     */
    @Query("SELECT * FROM cities WHERE id = :id LIMIT 1")
    suspend fun getCityById(id: Int): CityEntity?
}
