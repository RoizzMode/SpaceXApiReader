package com.example.spacexinfo.network

import com.example.spacexinfo.pojos.OneItemPOJO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OneApiInterface {

    @GET("launches/{flightNumber}")
    fun getLaunch(@Path("flightNumber") flightNumber: Int): Call<OneItemPOJO>
}