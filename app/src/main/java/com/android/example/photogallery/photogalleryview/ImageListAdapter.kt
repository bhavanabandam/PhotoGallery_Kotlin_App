package com.android.example.photogallery.photogalleryview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.photogallery.R
import com.android.example.photogallery.databinding.PhotoListItemBinding

class ImageListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<ImageEntity, ImageListAdapter.ImageItemViewHolder>(ImageEntityDiffCallBack()) {


    override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
        val imageEntity = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(imageEntity)
        }
        holder.binding.deleteIcon.setOnClickListener {
            onClickListener.ondeleteClick(imageEntity)
        }
        holder.bind(getItem(position))

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
        return ImageItemViewHolder.from(parent)
    }

    class ImageItemViewHolder(val binding: PhotoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ImageItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    PhotoListItemBinding.inflate(layoutInflater, parent, false)
                return ImageItemViewHolder(binding)
            }
        }

        fun bind(item: ImageEntity) {
            binding.image = item
            binding.executePendingBindings()
        }


    }

    interface OnClickListener {
        fun onClick(imageEntity: ImageEntity)
        fun ondeleteClick(imageEntity: ImageEntity)
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





