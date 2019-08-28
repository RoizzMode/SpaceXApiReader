package com.example.spacexinfo.pojos

import com.google.gson.annotations.SerializedName

class OneItemPOJO(
    @SerializedName("flight_number")
    val flightNumber: Int,
    @SerializedName("mission_name")
    val missionName: String,
    @SerializedName("launch_year")
    val launchYear: Int,
    @SerializedName("rocket")
    val rocket: RocketPOJO,
    @SerializedName("links")
    val links: LinkPOJO
)