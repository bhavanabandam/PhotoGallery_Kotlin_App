package com.android.example.photogallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

fun getBitMapFromString(imageString: String?): Bitmap {
    val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

fun getImageSize(imageUri: Bitmap): Float {
    val stream = ByteArrayOutputStream()
    imageUri.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val imageBytes = stream.toByteArray()
    return (imageBytes.size / 1024).toFloat()
}

fun getBitMapFromUri(uri: Uri?, context: Context?): Bitmap {
    val parcelFileDescriptor =
        uri?.let { context!!.contentResolver.openFileDescriptor(it, "r") }
    val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
    val imagebitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor.close();
    return imagebitmap

}

fun convertToBase64String(bitmap: Bitmap): String {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return android.util.Base64.encodeToString(b, Base64.DEFAULT)
}
