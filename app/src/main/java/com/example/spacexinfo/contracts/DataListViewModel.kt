package com.example.spacexinfo.contracts

import androidx.lifecycle.LiveData
import com.example.spacexinfo.data.LaunchesInfo
import com.example.spacexinfo.viewModels.Event

interface DataListViewModel: RecyclerScroller, RecyclerClickListener {
    val changeScreenWatcher: LiveData<Event<Int>>

    val listLaunches: LiveData<List<LaunchesInfo>>

    val recyclerVisibility: LiveData<Boolean>

    val progressBarVisibility: LiveData<Boolean>

    val failProgressVisibility: LiveData<Boolean>

    val retryButtonVisibility: LiveData<Boolean>

    val failMessageVisibility: LiveData<Boolean>


    fun viewCreated()

    fun viewStopped()

    fun viewResumed()

    fun retryButtonClicked()
}