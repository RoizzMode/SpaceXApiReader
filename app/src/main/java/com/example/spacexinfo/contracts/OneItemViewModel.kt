package com.example.spacexinfo.contracts

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.spacexinfo.data.OneLaunchInfo
import com.example.spacexinfo.viewModels.Event

interface OneItemViewModel {
    val oneLaunchInfo: LiveData<OneLaunchInfo>

    val photoVisibility: LiveData<Boolean>

    val noPhotoMessageVisibility: LiveData<Boolean>

    val infoVisibility: LiveData<Boolean>

    val progressBarVisibility: LiveData<Boolean>

    val retryButtonAndMessageVisibility: LiveData<Boolean>

    val failMessageVisibility: LiveData<Boolean>

    val oneLaunchPhoto: LiveData<Uri>

    val magnifierVisibility: LiveData<Boolean>

    val changeScreenIndicator: LiveData<Event<Boolean>>



    fun viewCreated(flightNumber: Int)

    fun viewStopped()

    fun retryButtonClicked()

    fun magnifierClicked()
}