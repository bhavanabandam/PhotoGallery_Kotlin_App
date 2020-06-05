package com.android.example.photogallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun getBitMapFromString(imageString: String?): Bitmap {
    val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}