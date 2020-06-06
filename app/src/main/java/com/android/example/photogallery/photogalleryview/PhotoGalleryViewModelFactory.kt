package com.android.example.photogallery.photogalleryview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.example.photogallery.database.ImageDatabaseDao
import com.android.example.photogallery.photodetails.PhotoDetailViewModel
import java.lang.IllegalArgumentException

class PhotoGalleryViewModelFactory(
    private val dataSource: ImageDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoGalleryViewModel::class.java)) {
            return PhotoGalleryViewModel(dataSource, application) as T
        }else if(modelClass.isAssignableFrom(PhotoDetailViewModel::class.java)){
            return PhotoDetailViewModel(dataSource,application) as T
        }

        throw IllegalArgumentException("Unknown View Model class")
    }
}