package com.android.example.photogallery.photogalleryview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.photogallery.R
import com.android.example.photogallery.databinding.PhotoListItemBinding

class ImageListAdapter :
    ListAdapter<ImageEntity, ImageListAdapter.ImageItemViewHolder>(ImageEntityDiffCallBack()) {


    override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
        val item = getItem(position)
        //holder.imageView.setImageDrawable(getBitmapFromString(item.imageUri))

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
        return ImageItemViewHolder.from(parent)
    }

    class ImageItemViewHolder(itemView: PhotoListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        companion object {
            fun from(parent: ViewGroup): ImageItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    PhotoListItemBinding.inflate(layoutInflater, parent, false)
                return ImageItemViewHolder(binding)
            }
        }

    }


}

class ImageEntityDiffCallBack : DiffUtil.ItemCallback<ImageEntity>() {
    override fun areItemsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
        return oldItem.imageId == newItem.imageId
    }

    override fun areContentsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
        return oldItem == newItem
    }
}


