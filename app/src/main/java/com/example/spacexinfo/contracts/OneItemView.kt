package com.example.spacexinfo.contracts

import com.example.spacexinfo.data.OneLaunchInfo

interface OneItemView {
    fun showInfo(launch: OneLaunchInfo)
    fun setInfoVisibility(visibility: Boolean)
    fun setProgressBarVisibility(visibility: Boolean)
    fun setFailMessageAndRetryButtonVisibility(visibility: Boolean)
}