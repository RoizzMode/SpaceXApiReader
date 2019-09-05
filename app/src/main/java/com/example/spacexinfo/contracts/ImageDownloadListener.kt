package com.example.spacexinfo.contracts

interface ImageDownloadListener {
    fun onStateChanged(isLoading: Boolean, isError: Boolean)
}