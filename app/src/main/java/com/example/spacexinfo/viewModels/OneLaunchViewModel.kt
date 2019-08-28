package com.example.spacexinfo.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spacexinfo.contracts.DataLoadListener
import com.example.spacexinfo.contracts.OneItemViewModel
import com.example.spacexinfo.data.OneLaunchInfo
import com.example.spacexinfo.data.SpaceXModel

class OneLaunchViewModel(private val model: SpaceXModel, private val isLarge: Boolean) : ViewModel(), OneItemViewModel {

    private lateinit var listener: DataLoadListener<OneLaunchInfo?>

    override val oneLaunchInfo = MutableLiveData<OneLaunchInfo>()

    override val oneLaunchPhoto = MutableLiveData<Uri>()

    override val infoVisibility = MutableLiveData<Boolean>()

    override val photoVisibility = MutableLiveData<Boolean>()

    override val progressBarVisibility = MutableLiveData<Boolean>()

    override val noPhotoMessageVisibility = MutableLiveData<Boolean>()

    override val retryButtonAndMessageVisibility = MutableLiveData<Boolean>()

    override val failMessageVisibility = MutableLiveData<Boolean>()

    override val magnifierVisibility = MutableLiveData<Boolean>()

    override val changeScreenIndicator = MutableLiveData<Event<Boolean>>()


    override fun viewCreated(flightNumber: Int) {
        listener = object : DataLoadListener<OneLaunchInfo?> {
            override fun onDataLoadingEvent(data: OneLaunchInfo?, isLoading: Boolean, isError: Boolean) {

                failMessageVisibility.value = isError

                if (isLoading) {
                    infoVisibility.value = false
                    retryButtonAndMessageVisibility.value = false
                    progressBarVisibility.value = true
                    return
                }

                if (isError) {
                    progressBarVisibility.value = false

                }

                if (data == null) {
                    progressBarVisibility.value = false
                    retryButtonAndMessageVisibility.value = true
                } else {
                    infoVisibility.value = true
                    oneLaunchInfo.value = data
                    val photo = model.getPhoto()
                    if (photo != null) {
                        magnifierVisibility.value = true
                        noPhotoMessageVisibility.value = false
                        photoVisibility.value = true
                        oneLaunchPhoto.value = photo
                    } else {
                        photoVisibility.value = false
                        magnifierVisibility.value = false
                        noPhotoMessageVisibility.value = true
                    }
                    progressBarVisibility.value = false
                }
            }
        }

        model.flightNumber = flightNumber
        model.setOnOneItemLoadListener(listener)
        model.loadOneLaunch()
    }

    override fun viewStopped() {
        model.removeOnOneItemLoadListener(listener)
    }

    override fun retryButtonClicked() {
        retryButtonAndMessageVisibility.value = false
        progressBarVisibility.value = true
        model.loadOneLaunch()
    }

    override fun magnifierClicked() {
        changeScreenIndicator.value = Event(true)
    }
}