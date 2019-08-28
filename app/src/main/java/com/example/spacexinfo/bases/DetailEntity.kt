package com.example.spacexinfo.bases

import androidx.room.*

@Entity(tableName = "Details", indices = [Index(value = ["flightNumber"], unique = true)], foreignKeys = [ForeignKey(entity = LaunchesEntity::class, parentColumns = ["flightNumber"], childColumns = ["flightNumber"])])
class DetailEntity(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                   @ColumnInfo(name = "flightNumber")
                   val flightNumber: Int,
                   val coreSerial: String?,
                   val flight: Int?,
                   val wasReused: Boolean?,
                   val rocketName: String?,
                   val rocketType: String?,
                   val blocks: Int?,
                   val payloadId: String?,
                   val payloadType: String?,
                   val nationality: String?,
                   val manufacturer: String?,
                   val photoURL: String?
)