package com.android.example.photogallery.photogallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.example.photogallery.R
import com.android.example.photogallery.databinding.PhotoGalleryFragmentBinding

class PhotoGalleryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: PhotoGalleryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.photo_gallery_fragment, container, false)
        return binding.root
    }
}