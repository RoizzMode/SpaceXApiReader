package com.example.spacexinfo.contracts

interface OneItemPresenter {
    fun attachView(currentView: OneItemView)
    fun viewCreated(flightNumber: Int)
    fun viewStopped()
    fun retryButtonClicked()
}