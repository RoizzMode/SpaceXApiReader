package com.example.spacexinfo.bases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spacexinfo.bases.AppDatabase.Companion.DATABASE_VERSION

@Database(entities = [LaunchesEntity::class, DetailEntity::class], version = DATABASE_VERSION, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun launchesDao(): LaunchesDAO
    abstract fun detailsDao(): DetailDAO


    companion object {
        const val DATABASE_VERSION = 1
    }
}