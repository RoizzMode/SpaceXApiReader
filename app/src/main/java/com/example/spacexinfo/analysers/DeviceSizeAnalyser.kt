package com.example.spacexinfo.analysers

import android.content.Context
import android.content.res.Configuration

class DeviceSizeAnalyser(private val context: Context) {
    fun isLarge(): Boolean {
        if ((context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) > Configuration.SCREENLAYOUT_SIZE_LARGE)
            return true
        return false
    }
}