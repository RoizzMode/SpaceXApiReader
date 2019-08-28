package com.example.spacexinfo.network

import com.example.spacexinfo.data.SpaceXModel
import com.example.spacexinfo.pojos.OneItemPOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OneItemRepository(private val model: SpaceXModel) {

    private lateinit var launch: OneItemPOJO

    fun loadData(flightNumber: Int) {
        val call = NetworkService().clientForOne.getLaunch(flightNumber)
        call.enqueue(object : Callback<OneItemPOJO> {

            override fun onResponse(call: Call<OneItemPOJO>, response: Response<OneItemPOJO>) {
                launch = response.body() ?: throw NullPointerException()
                model.oneItemLoaded()
            }

            override fun onFailure(call: Call<OneItemPOJO>, t: Throwable) {
                model.loadOneFailed()
            }
        })
    }

    fun getData(): OneItemPOJO {
        return launch
    }
}