package com.example.spacexinfo.viewModels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spacexinfo.R
import com.example.spacexinfo.contracts.DataLoadListener
import com.example.spacexinfo.contracts.ImageDownloadListener
import com.example.spacexinfo.contracts.ImageViewModel
import com.example.spacexinfo.data.SnackbarInfo
import com.example.spacexinfo.data.SpaceXModel
import com.google.android.material.snackbar.Snackbar

class ImageActivityViewModel(private val model: SpaceXModel): ViewModel(), ImageViewModel {

    private var listenerDownload: ImageDownloadListener? = null
    private var listener: DataLoadListener<Uri?>? = null

    override val uri = MutableLiveData<Uri>()
    override val downloadParams = MutableLiveData<Event<Boolean>>()
    override val snackBarMessage = MutableLiveData<SnackbarInfo>()
    override val imageViewVisibility = MutableLiveData<Boolean>()
    override val failMessageVisibility = MutableLiveData<Boolean>()

    override fun downloadButtonClicked(bmp: Bitmap){
        model.bmp = bmp
        downloadParams.value = Event(true)
    }

    override fun fileCreated(uri: Uri){
        listenerDownload = object : ImageDownloadListener{
            override fun onStateChanged(isLoading: Boolean, isError: Boolean) {
                if (isError)
                    snackBarMessage.value = SnackbarInfo(Snackbar.LENGTH_SHORT, model.resources?.getString(R.string.fail_download) ?: "Null")
                if (isLoading)
                    snackBarMessage.value = SnackbarInfo(Snackbar.LENGTH_INDEFINITE, model.resources?.getString(R.string.loading_message) ?: "Null")
                if (!isError && !isLoading)
                    snackBarMessage.value = SnackbarInfo(Snackbar.LENGTH_SHORT, model.resources?.getString(R.string.download_success) ?: "Null")
                model.removeImageDownloadListener(listenerDownload)
            }
        }
        model.setImageDownloadListener(listenerDownload)
        model.downloadImage(uri)
    }

    override fun openWithButtonClicked(bmp: Bitmap, name: String){
        listener = object : DataLoadListener<Uri?>{
            override fun onDataLoadingEvent(data: Uri?, isLoading: Boolean, isError: Boolean) {
                uri.value = data
                model.removeImageListener(listener)
            }
        }
        model.setImageListener(listener)
        model.createFileInCacheAndGetUri(bmp, name)
    }

    override fun viewStopped(){
        if (listenerDownload != null)
            model.removeImageDownloadListener(listenerDownload)
        if (listener != null)
            model.removeImageListener(listener)
    }

    override fun uriOrNameWasntPassed() {
        imageViewVisibility.value = false
        failMessageVisibility.value = true
    }

    override fun fileCouldntCreate() {
        snackBarMessage.value = SnackbarInfo(Snackbar.LENGTH_SHORT, model.resources?.getString(R.string.file_couldnt_create) ?: "Null")
    }
}