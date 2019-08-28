package com.example.spacexinfo.data

import android.app.Application
import androidx.room.Room
import com.example.spacexinfo.bases.AppDatabase

class SpaceXApplication: Application() {
    private lateinit var appDatabase: AppDatabase
    lateinit var spaceXModel: SpaceXModel

    override fun onCreate() {
        super.onCreate()
        appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, DB_NAME).build()
        spaceXModel = SpaceXModel(this, appDatabase)
        spaceXModel.initModel()
    }

    companion object{
        private const val DB_NAME = "appDB"
    }
}