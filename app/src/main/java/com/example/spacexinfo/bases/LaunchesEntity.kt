package com.example.spacexinfo.bases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Launches", indices = [Index(value = ["flightNumber", "missionName"], unique = true)])
class LaunchesEntity(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                     @ColumnInfo(name = "flightNumber")
                     val flightNumber: Int,
                     @ColumnInfo(name = "missionName")
                     val missionName: String,
                     val launchYear: Int)