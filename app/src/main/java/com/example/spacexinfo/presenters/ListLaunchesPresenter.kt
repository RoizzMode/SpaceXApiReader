package com.example.spacexinfo.presenters

import com.example.spacexinfo.contracts.DataListPresenter
import com.example.spacexinfo.contracts.DataListView
import com.example.spacexinfo.contracts.DataLoadListener
import com.example.spacexinfo.data.SpaceXModel

class ListLaunchesPresenter(private val model: SpaceXModel, private val isLarge: Boolean): DataListPresenter {

    private lateinit var view: DataListView
    private lateinit var loadListener: DataLoadListener
    private var isLoading = false
    private var isLast = false

    override fun attachView(currentView: DataListView){
        view = currentView
    }

    override fun listScrolled() {
        if (!isLoading && !isLast) {
            view.setProgressBarVisibility(true)
            model.loadLaunches()
            isLoading = true
        }
    }

    override fun viewCreated() {
        loadListener = object : DataLoadListener{
            override fun onDataLoaded() {
                view.showItems(model.getLaunches())
                isLoading = false
                view.setProgressBarVisibility(false)
                if (model.isLast)
                    isLast = true
            }
        }
        model.setOnDataLoadListener(loadListener)
        model.loadLaunches()
    }

    override fun viewReturned() {
        loadListener = object : DataLoadListener{
            override fun onDataLoaded() {
                view.showItems(model.getLaunches())
                isLoading = false
                view.setProgressBarVisibility(false)
                if (model.isLast)
                    isLast = true
            }
        }
        model.setOnDataLoadListener(loadListener)
        view.showItems(model.getLaunches())
    }

    override fun itemClicked(position: Int) {
        if (!isLarge) {
            view.changeScreen(model.findNumber(position))
        }
        else{
            val flightNumber = model.findNumber(position)
            model.loadOneLaunch(flightNumber)
        }
    }

    override fun viewStopped() {
        model.removeOnDataLoadListener(loadListener)
    }
}