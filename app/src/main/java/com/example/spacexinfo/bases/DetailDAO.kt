package com.example.spacexinfo.bases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DetailDAO {
    @Query("SELECT * FROM Details WHERE flightNumber == :flightNumber")
    fun getDetail(flightNumber: String): List<DetailEntity>

    @Insert
    fun insert(detailEntity: DetailEntity)

    @Update
    fun update(detailEntity: DetailEntity): Int
}