package com.android.example.photogallery.photodetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.example.photogallery.photogalleryview.ImageDatabaseDao
import com.android.example.photogallery.photogalleryview.ImageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoDetailViewModel(val dataBase: ImageDatabaseDao, application: Application) : AndroidViewModel(application) {

    val _selectedImageDetails = MutableLiveData<ImageEntity>()
    val selectImageDetails: LiveData<ImageEntity>
        get() = _selectedImageDetails
    var isDeleted = MutableLiveData<Boolean>()


    private val uiScope = CoroutineScope(Dispatchers.Main)

    init {
        isDeleted.value = false
    }

    fun getImageDetails(imageId: Int) {
        uiScope.launch {
            _selectedImageDetails.value = getImageFromDb(imageId)
        }
    }

    private suspend fun getImageFromDb(imageId: Int): ImageEntity? {
        return withContext(Dispatchers.IO) {
            var imageEntity = dataBase.findImageByPrimaryKey(imageId)
            imageEntity
        }

    }

    fun deleteImageAndNavigate(){
        uiScope.launch {
            deleteImageFromDb()
            isDeleted.value = true
        }
    }

    private suspend fun deleteImageFromDb() {
        withContext(Dispatchers.IO){
            dataBase.delete(_selectedImageDetails.value!!)

        }
    }


}