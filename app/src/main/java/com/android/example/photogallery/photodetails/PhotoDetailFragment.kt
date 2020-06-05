package com.android.example.photogallery.photodetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.android.example.photogallery.R
import com.android.example.photogallery.databinding.PhotoDetailsFragmentBinding
import com.android.example.photogallery.getBitMapFromString
import com.android.example.photogallery.photogalleryview.ImageDataBase
import com.android.example.photogallery.photogalleryview.PhotoGalleryViewModel
import com.android.example.photogallery.photogalleryview.PhotoGalleryViewModelFactory

class PhotoDetailFragment : Fragment() {

    private lateinit var viewModel: PhotoDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: PhotoDetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.photo_details_fragment, container, false)
        val application = requireNotNull(this.activity).application
        val imageDatabaseDao = ImageDataBase.getInstance(application).imageDatabaseDao
        val viewModelFactory = PhotoGalleryViewModelFactory(imageDatabaseDao, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(PhotoDetailViewModel::class.java)
        binding.detailViewModel = viewModel
        val args = PhotoDetailFragmentArgs.fromBundle(arguments!!)
        viewModel.getImageDetails(args.imageId)
        viewModel.selectImageDetails.observe(viewLifecycleOwner, Observer {
            binding.mainPhotoImage.setImageBitmap(getBitMapFromString(it.imageUri))
            binding.imageSize.setText("Image Size:" + it.imageSize)
        })
        viewModel.isDeleted.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController()
                    .navigate(PhotoDetailFragmentDirections.actionPhotoDetailFragmentToPhotoGalleryFragment())
                viewModel.isDeleted.value = false
            }
        })
        return binding.root
    }

}