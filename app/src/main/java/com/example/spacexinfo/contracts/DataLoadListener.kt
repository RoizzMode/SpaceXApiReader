package com.example.spacexinfo.contracts

interface DataLoadListener <T> {
    fun onDataLoadingEvent(data: T, isLoading: Boolean, isError: Boolean)
}