package com.android.example.photogallery.photogalleryview

import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.photogallery.getBitMapFromString

@BindingAdapter("imageBitmap")
fun ImageView.setImageBitmap(imageUri: String?) {
    imageUri?.let {
        setImageBitmap(getBitMapFromString(imageUri))
    }

}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,
                     data: List<ImageEntity>?) {
    val adapter = recyclerView.adapter as ImageListAdapter
    adapter.submitList(data)
}

@BindingAdapter("deleteIconClick")
fun ImageView.setOnclick(item:ImageEntity){
    setOnClickListener {

    }
}