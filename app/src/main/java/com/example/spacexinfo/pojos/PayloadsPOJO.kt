package com.example.spacexinfo.pojos

import com.google.gson.annotations.SerializedName

class PayloadsPOJO(
    @SerializedName("payload_id")
    val payloadId: String?,
    @SerializedName("payload_type")
    val payloadType: String?,
    @SerializedName("nationality")
    val nationality: String?,
    @SerializedName("manufacturer")
    val manufacturer: String?
)