package com.android.example.photogallery.photogalleryview

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ImageDatabaseDao {
    @Insert
    fun insert(imageDB: ImageEntity)

    @Query("SELECT * FROM image_galley_table ORDER BY imageId DESC")
    fun getAllImages(): LiveData<List<ImageEntity>>

    @Delete
    fun delete(imageDB: ImageEntity)
}