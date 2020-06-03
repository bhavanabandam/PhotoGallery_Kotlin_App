package com.android.example.photogallery.photogalleryview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class PhotoGalleryViewModel() : ViewModel() {

    private val uiScope = CoroutineScope(Dispatchers.Main)





    init {

    }

    fun onUploadClick() {

    }

    override fun onCleared() {
        super.onCleared()
    }
}