package com.tablegroup.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tablegroup.data.local.room.dao.CityDao
import com.tablegroup.data.local.room.entities.CityEntity

/**
 * Room database definition for the app.
 *
 * Holds the list of entities and exposes DAOs.
 */
@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Provides access to City DAO for database operations related to CityEntity.
     */
    abstract fun cityDao(): CityDao
}
