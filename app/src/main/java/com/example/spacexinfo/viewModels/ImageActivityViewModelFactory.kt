package com.example.spacexinfo.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spacexinfo.data.SpaceXModel

class ImageActivityViewModelFactory(private val model: SpaceXModel): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ImageActivityViewModel(model) as T
    }
}