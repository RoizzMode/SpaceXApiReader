package com.example.spacexinfo.pojos

import com.google.gson.annotations.SerializedName

class SecondStagePOJO(
    @SerializedName("block")
    val block: Int?,
    @SerializedName("payloads")
    val payloads: List<PayloadsPOJO>
)