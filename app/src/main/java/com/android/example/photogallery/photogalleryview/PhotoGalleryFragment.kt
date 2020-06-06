package com.android.example.photogallery.photogalleryview

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.example.photogallery.R
import com.android.example.photogallery.convertToBase64String
import com.android.example.photogallery.database.ImageDataBase
import com.android.example.photogallery.database.ImageEntity
import com.android.example.photogallery.databinding.PhotoGalleryFragmentBinding
import com.android.example.photogallery.getBitMapFromUri
import com.android.example.photogallery.getImageSize
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import java.io.ByteArrayOutputStream

class PhotoGalleryFragment : Fragment(), ImageListAdapter.OnClickListener {

    private lateinit var viewModel: PhotoGalleryViewModel


    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1002

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: PhotoGalleryFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.photo_gallery_fragment, container, false)
        val application = requireNotNull(this.activity).application
        val imageDatabaseDao = ImageDataBase.getInstance(application).imageDatabaseDao
        val viewModelFactory = PhotoGalleryViewModelFactory(imageDatabaseDao, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(PhotoGalleryViewModel::class.java)
        binding.viewModel = viewModel

        val gridLayoutManager = GridLayoutManager(activity, 3)
        binding.photoList.layoutManager = gridLayoutManager

        val adapter = ImageListAdapter(this)
        viewModel.images.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    adapter.submitList(it)
                }
            }
        )
        binding.photoList.adapter = adapter


        binding.uploadButton.setOnClickListener {
            //show bottom sheet and select picture and update the recycler view with Live Data concept
            showBottomSheetDialog()
        }
        viewModel.navigateToSelectedImage.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                    PhotoGalleryFragmentDirections.actionPhotoGalleryFragmentToPhotoDetailFragment(
                        it.imageId!!
                    )
                )
                viewModel.displayImageDetailsComplete();

            }
        })
        return binding.root
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(context!!)
        val bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        dialog.setContentView(bottomSheet)
        bottomSheet.textView2.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity!!.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    activity!!.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    //permission was not enabled
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    //show popup to request permission

                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    launchCamera()

                }
            } else {
                launchCamera()
            }
            dialog.dismiss()

        }

        bottomSheet.textView3.setOnClickListener {
            pickFromGallery()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, REQUEST_SELECT_IMAGE_IN_ALBUM)

    }

    private fun launchCamera() {
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    launchCamera()
                } else {
                    //permission from popup was denied
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK) {
            val imageUri: Bitmap
            val imageName : String
            if (requestCode == IMAGE_CAPTURE_CODE) {
                imageUri = data?.extras?.get("data") as Bitmap
            } else if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
                val uri = data?.data
                imageUri = getBitMapFromUri(uri,context)
            } else {
                imageUri =
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
                super.onActivityResult(requestCode, resultCode, data)
            }
            viewModel.insertImageIntoDB(convertToBase64String(imageUri),getImageSize(imageUri))

        }
    }

    override fun onClick(imageEntity: ImageEntity) {
        viewModel.displayImageDetails(imageEntity)
    }

    override fun ondeleteClick(imageEntity: ImageEntity) {
        viewModel.deleteImageFromDb(imageEntity)
    }

}