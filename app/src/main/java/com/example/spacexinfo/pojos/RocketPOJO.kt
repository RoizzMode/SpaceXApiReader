package com.example.spacexinfo.pojos

import com.google.gson.annotations.SerializedName

class RocketPOJO(
    @SerializedName("rocket_name")
    val rocketName: String?,
    @SerializedName("rocket_type")
    val rocketType: String?,
    @SerializedName("first_stage")
    val firstStage: FirstStagePOJO,
    @SerializedName("second_stage")
    val secondStage: SecondStagePOJO
)