package com.android.example.photogallery.photogalleryview

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.photogallery.database.ImageEntity
import com.android.example.photogallery.getBitMapFromString

@BindingAdapter("imageBitmap")
fun ImageView.setImageBitmap(imageUri: String?) {
    imageUri?.let {
        setImageBitmap(getBitMapFromString(imageUri))
    }

}

@BindingAdapter("deleteIconClick")
fun ImageView.setOnclick(item: ImageEntity){
    setOnClickListener {

    }
}