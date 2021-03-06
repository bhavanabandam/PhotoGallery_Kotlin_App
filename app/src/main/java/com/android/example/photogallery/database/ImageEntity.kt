package com.android.example.photogallery.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_galley_table")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val imageId: Int? = null,

    @ColumnInfo(name = "image_uri")
    val imageUri: String?,

    @ColumnInfo(name = "image_size")
    val imageSize: Float?



)