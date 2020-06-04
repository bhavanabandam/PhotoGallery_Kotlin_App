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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.android.example.photogallery.R
import com.android.example.photogallery.databinding.PhotoGalleryFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog.view.*
import java.io.ByteArrayOutputStream

class PhotoGalleryFragment : Fragment() {

    private lateinit var viewModel: PhotoGalleryViewModel

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

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
        val gridLayoutManager = GridLayoutManager(activity, 3)
        binding.photoList.layoutManager = gridLayoutManager
        val adapter = ImageListAdapter()
        viewModel.images.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    Toast.makeText(activity, "selected ${it.size}", Toast.LENGTH_SHORT).show()
                    adapter.submitList(it)
                }
            }
        )
        binding.photoList.adapter = adapter

        binding.uploadButton.setOnClickListener {
            //show bottom sheet and select picture and update the recycler view with Live Data concept
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
        return binding.root
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
            if (requestCode == IMAGE_CAPTURE_CODE) {
                imageUri = data?.extras?.get("data") as Bitmap
            } else if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
                val uri = data?.data
                imageUri = getBitMapFromUri(uri)
            } else {
                imageUri =
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
                super.onActivityResult(requestCode, resultCode, data)
            }



            viewModel.insertImageIntoDB(convertToBase64String(imageUri))
            Toast.makeText(activity, "selected $imageUri", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBitMapFromUri(uri: Uri?): Bitmap {
        val parcelFileDescriptor =
            uri?.let { context!!.contentResolver.openFileDescriptor(it, "r") }
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val imagebitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close();
        return imagebitmap

    }

    private fun convertToBase64String(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return android.util.Base64.encodeToString(b, Base64.DEFAULT)
    }

}