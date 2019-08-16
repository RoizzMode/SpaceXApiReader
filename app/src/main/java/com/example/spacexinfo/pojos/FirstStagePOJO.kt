package com.example.spacexinfo.pojos

import com.google.gson.annotations.SerializedName

class FirstStagePOJO(
    @SerializedName("cores")
    val cores: List<CoresPOJO>
)