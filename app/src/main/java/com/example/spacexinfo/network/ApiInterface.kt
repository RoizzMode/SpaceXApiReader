package com.example.spacexinfo.network

import com.example.spacexinfo.pojos.LaunchListPOJO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("launches")
    fun getLaunches(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<List<LaunchListPOJO>>
}