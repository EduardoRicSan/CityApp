package com.tablegroup.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tablegroup.data.local.room.entities.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {


    @Query("SELECT * FROM cities ORDER BY LOWER(name), LOWER(country)")
    suspend fun getAllCities(): List<CityEntity>

    @Query("SELECT * FROM cities ORDER BY LOWER(name), LOWER(country)")
    fun getAllCitiesFlow(): Flow<List<CityEntity>>  // Agregado para flujo reactivo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("SELECT * FROM cities WHERE id = :id LIMIT 1")
    suspend fun getCityById(id: Int): CityEntity?
}