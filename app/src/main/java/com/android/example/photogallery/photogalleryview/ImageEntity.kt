package com.android.example.photogallery.photogalleryview

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_galley_table")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val imageId: Int? = null,

    @ColumnInfo(name = "image_uri")
    val imageUri: String?

)