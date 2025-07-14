package com.tablegroup.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tablegroup.data.local.room.dao.CityDao
import com.tablegroup.data.local.room.entities.CityEntity

@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}