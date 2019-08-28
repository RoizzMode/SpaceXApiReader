package com.example.spacexinfo.pojos

import com.google.gson.annotations.SerializedName

class CoresPOJO(
    @SerializedName("core_serial")
    val coreSerial: String?,
    @SerializedName("flight")
    val flight: Int?,
    @SerializedName("reused")
    val reused: Boolean?

)