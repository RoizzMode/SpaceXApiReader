package com.example.spacexinfo.contracts

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.spacexinfo.data.SnackbarInfo
import com.example.spacexinfo.viewModels.Event

interface ImageViewModel {
    val uri: LiveData<Uri>
    val downloadParams: LiveData<Event<Boolean>>
    val snackBarMessage: LiveData<SnackbarInfo>
    val imageViewVisibility: LiveData<Boolean>
    val failMessageVisibility: LiveData<Boolean>
    fun downloadButtonClicked(bmp: Bitmap)
    fun fileCreated(uri: Uri)
    fun openWithButtonClicked(bmp: Bitmap, name: String)
    fun viewStopped()
    fun uriOrNameWasntPassed()
    fun fileCouldntCreate()
}