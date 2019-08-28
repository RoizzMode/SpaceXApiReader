package com.example.spacexinfo.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spacexinfo.contracts.DataListViewModel
import com.example.spacexinfo.contracts.DataLoadListener
import com.example.spacexinfo.data.LaunchesInfo
import com.example.spacexinfo.data.SpaceXModel

class ListLaunchesViewModel(private val model: SpaceXModel, private val isLarge: Boolean) : ViewModel(),
    DataListViewModel {

    private lateinit var loadListener: DataLoadListener<List<LaunchesInfo>>

    override val listLaunches = MutableLiveData<List<LaunchesInfo>>()

    override val recyclerVisibility = MutableLiveData<Boolean>()

    override val progressBarVisibility = MutableLiveData<Boolean>()

    override val failProgressVisibility = MutableLiveData<Boolean>()

    override val changeScreenWatcher = MutableLiveData<Event<Int>>()

    override val retryButtonVisibility = MutableLiveData<Boolean>()

    override val failMessageVisibility = MutableLiveData<Boolean>()

    override fun listScrolled() {
        progressBarVisibility.value = true
        model.loadLaunches()
    }

    override fun viewCreated() {
        loadListener = object : DataLoadListener<List<LaunchesInfo>> {
            override fun onDataLoadingEvent(data: List<LaunchesInfo>, isLoading: Boolean, isError: Boolean) {
                if (isError && data.isEmpty())
                    retryButtonVisibility.value = true
                if (!isError) {
                    retryButtonVisibility.value = false

                    failProgressVisibility.value = false

                    failMessageVisibility.value = false

                    recyclerVisibility.value = true

                    listLaunches.value = data

                    progressBarVisibility.value = false
                }
            }
        }
        model.setOnDataLoadListener(loadListener)
    }

    override fun itemClicked(position: Int) {
        val flightNumber = model.findNumber(position)
        model.flightNumber = flightNumber
        if (isLarge)
            model.loadOneLaunch()
        else
            changeScreenWatcher.value = Event(model.findNumber(position))
    }

    override fun viewStopped() {
        model.removeOnDataLoadListener(loadListener)
    }

    override fun retryButtonClicked() {
        model.loadLaunches()
    }
}