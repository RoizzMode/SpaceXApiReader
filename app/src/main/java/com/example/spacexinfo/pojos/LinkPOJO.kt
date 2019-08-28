package com.example.spacexinfo.pojos

import com.google.gson.annotations.SerializedName

class LinkPOJO(
    @SerializedName("flickr_images")
    val flickrImages: List<String?>
)