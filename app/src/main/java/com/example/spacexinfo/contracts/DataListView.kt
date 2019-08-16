package com.example.spacexinfo.contracts

import com.example.spacexinfo.data.LaunchesInfo

interface DataListView {
    fun showItems(launchListList: List<LaunchesInfo>)
    fun changeScreen(flightNumber: Int)
    fun lastItemLoaded() // setProgressBarVisibility()
    fun setProgressBarVisibility(visibility: Boolean)
}