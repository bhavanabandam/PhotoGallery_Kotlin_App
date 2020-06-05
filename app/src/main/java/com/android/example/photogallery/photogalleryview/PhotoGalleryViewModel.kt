package com.android.example.photogallery.photogalleryview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoGalleryViewModel(val dataBase: ImageDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    private val uiScope = CoroutineScope(Dispatchers.Main)

    private val _navigateToSelectedImage = MutableLiveData<ImageEntity>()
    val navigateToSelectedImage: LiveData<ImageEntity>
        get() = _navigateToSelectedImage

    val images = dataBase.getAllImages()


    init {

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun insertImageIntoDB(imageUri: String, imageSize: Float) {
        uiScope.launch {
            val imageEntitydata = ImageEntity(imageUri = imageUri,imageSize = imageSize)
            inserToRoom(imageEntitydata)
        }
    }

    private suspend fun inserToRoom(imageEntitydata: ImageEntity) {
        withContext(Dispatchers.IO) {
            dataBase.insert(imageEntitydata)
        }
    }

    fun displayImageDetails(imagedetails: ImageEntity) {
        _navigateToSelectedImage.value = imagedetails
    }

    fun displayImageDetailsComplete() {
        _navigateToSelectedImage.value = null
    }

    fun deleteImageFromDb(imageEntity: ImageEntity) {
        uiScope.launch {
            deleteFromRoom(imageEntity)
        }
    }

    private suspend fun deleteFromRoom(imageEntity: ImageEntity) {
        withContext(Dispatchers.IO) {
            dataBase.delete(imageEntity)
        }
    }


}