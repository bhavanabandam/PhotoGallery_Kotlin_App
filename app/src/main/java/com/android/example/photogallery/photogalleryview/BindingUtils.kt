package com.android.example.photogallery.photogalleryview

import android.media.Image
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.android.example.photogallery.getBitMapFromString

@BindingAdapter("imageBitmap")
fun ImageView.setImageBitmap(item: ImageEntity) {
    setImageBitmap(item.imageUri?.let { getBitMapFromString(it) })
}