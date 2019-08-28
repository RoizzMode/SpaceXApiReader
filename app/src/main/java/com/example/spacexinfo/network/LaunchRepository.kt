package com.example.spacexinfo.network

import com.example.spacexinfo.consts.ScrollConsts
import com.example.spacexinfo.contracts.DataLoader
import com.example.spacexinfo.data.SpaceXModel
import com.example.spacexinfo.pojos.LaunchListPOJO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaunchRepository(private val model: SpaceXModel) : DataLoader {

    var isLast = false
    private val launchList = arrayListOf<LaunchListPOJO>()

    override fun loadData(from: Int) {
            val call = NetworkService().client.getLaunches(from, ScrollConsts.PAGE_SIZE)
            call.enqueue(object : Callback<List<LaunchListPOJO>> {

                override fun onResponse(call: Call<List<LaunchListPOJO>>, response: Response<List<LaunchListPOJO>>) {
                    if (response.body()?.isEmpty() ?: throw NullPointerException())
                        isLast = true
                    launchList.clear()
                    launchList.addAll(response.body() ?: throw NullPointerException())
                    model.launchListLoaded()
                }

                override fun onFailure(call: Call<List<LaunchListPOJO>>, t: Throwable) {
                    model.loadFailed()
                }
            })
    }

    override fun getData(): List<LaunchListPOJO> {
        return launchList
    }
}