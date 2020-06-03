package com.android.example.photogallery.photogalleryview

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoGalleryViewModel(val dataBase: ImageDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    private val uiScope = CoroutineScope(Dispatchers.Main)

    private val uploadedImage = MutableLiveData<ImageEntity>()

    private var id: Int = 0
    val images = dataBase.getAllImages()


    init {
    }

    fun onUploadClick() {

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun insertImageIntoDB(imageUri: String) {
        uiScope.launch {
            val imageEntitydata = ImageEntity(id++, imageUri)
            inserToRoom(imageEntitydata)


        }
    }

    private suspend fun inserToRoom(imageEntitydata: ImageEntity) {
        withContext(Dispatchers.IO) {
            dataBase.insert(imageEntitydata)
        }
    }
}