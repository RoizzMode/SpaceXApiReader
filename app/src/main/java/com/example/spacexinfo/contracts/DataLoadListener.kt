package com.example.spacexinfo.contracts

interface DataLoadListener <T> {
    //fun onDataLoaded(offlineMode: Boolean)
    fun onDataLoadingEvent(data: T, isLoading: Boolean, isError: Boolean)
}