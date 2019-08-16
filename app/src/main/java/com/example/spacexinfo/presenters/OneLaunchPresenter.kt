package com.example.spacexinfo.presenters

import com.example.spacexinfo.contracts.DataLoadListener
import com.example.spacexinfo.contracts.OneItemPresenter
import com.example.spacexinfo.contracts.OneItemView
import com.example.spacexinfo.data.SpaceXModel

class OneLaunchPresenter(private val model: SpaceXModel, private val isLarge: Boolean): OneItemPresenter {

    private lateinit var view: OneItemView
    private lateinit var listener: DataLoadListener
    private lateinit var loadingListener: DataLoadListener
    private lateinit var failListener: DataLoadListener
    private var flightNum = 1

    override fun attachView(currentView: OneItemView) {
        view = currentView
    }

    override fun viewCreated(flightNumber: Int) {
        if (isLarge)
            passListenerToModel()
        listener = object : DataLoadListener{
            override fun onDataLoaded() {
                view.showInfo(model.getOneLaunch())
                view.setProgressBarVisibility(false)
                view.setInfoVisibility(true)
            }
        }
        failListener = object : DataLoadListener{
            override fun onDataLoaded() {
                view.setProgressBarVisibility(false)
                view.setFailMessageAndRetryButtonVisibility(true)
            }
        }
        model.setOnLoadFailListener(failListener)
        model.setOnOneItemLoadListener(listener)
        flightNum = flightNumber
        model.loadOneLaunch(flightNumber)
    }

    private fun passListenerToModel(){
        loadingListener = object : DataLoadListener{
            override fun onDataLoaded() {
                view.setInfoVisibility(false)
                view.setProgressBarVisibility(true)
            }
        }
        model.setOnDataStartedLoadingListener(loadingListener)
    }

    override fun viewStopped() {
        model.removeOnOneItemLoadListener(listener)
        model.removeOnLoadFailListener(failListener)
        if (isLarge)
            model.removeOnDataStartedLoadingListener(loadingListener)
    }

    override fun retryButtonClicked() {
        view.setFailMessageAndRetryButtonVisibility(false)
        view.setProgressBarVisibility(true)
        model.loadOneLaunch(flightNum)
    }
}