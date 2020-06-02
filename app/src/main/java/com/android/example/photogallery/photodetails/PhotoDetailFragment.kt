package com.android.example.photogallery.photodetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.example.photogallery.R
import com.android.example.photogallery.databinding.PhotoDetailsFragmentBinding

class PhotoDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: PhotoDetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.photo_details_fragment, container, false)
        return binding.root
    }
}