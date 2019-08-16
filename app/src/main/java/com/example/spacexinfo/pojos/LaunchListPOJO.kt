package com.example.spacexinfo.pojos

import com.google.gson.annotations.SerializedName

class LaunchListPOJO(
    @SerializedName("flight_number")
    val flightNumber: Int,
    @SerializedName("mission_name")
    val missionName: String,
    @SerializedName("launch_year")
    val launchYear: Int
)