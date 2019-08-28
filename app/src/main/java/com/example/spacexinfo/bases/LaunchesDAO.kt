package com.example.spacexinfo.bases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LaunchesDAO {
    @Query("SELECT * FROM Launches")
    fun getAllLaunches(): List<LaunchesEntity>

    @Insert
    fun insert(launchesEntity: LaunchesEntity)

    @Query("SELECT * FROM Launches WHERE flightNumber == :fn")
    fun getLaunchMainInfo(fn: String): List<LaunchesEntity>
}