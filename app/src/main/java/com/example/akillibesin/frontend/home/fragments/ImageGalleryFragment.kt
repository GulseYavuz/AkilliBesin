package com.example.akillibesin.frontend.home.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.ViewModelProvider
import com.example.akillibesin.databinding.FragmentImageGalleryBinding

class ImageGalleryFragment : Fragment() {
    private lateinit var binding: FragmentImageGalleryBinding
    private lateinit var imageViewModel: ImageViewModel

    companion object {
        @JvmStatic
        fun newInstance() = ImageGalleryFragment().apply {}
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openGallery()
        } else {
            Log.e("ImageGalleryFragment", "Permission denied")
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                val bitmap = getBitmapFromUri(selectedImageUri)
                handleSelectedImage(bitmap)
                imageViewModel.selectedImageBitmap = bitmap // Store the bitmap in the ViewModel
            } else {
                Log.e("ImageGalleryFragment", "Selected image URI is null")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun handleSelectedImage(bitmap: Bitmap?) {
        if (bitmap != null) {
            binding.imageView.setImageBitmap(bitmap)
            binding.imageView.visibility = View.VISIBLE
        } else {
            Log.e("ImageGalleryFragment", "Bitmap is null")
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.copy(Bitmap.Config.ARGB_8888, true)
            }
        } catch (e: Exception) {
            Log.e("ImageGalleryFragment", "Failed to load image from URI", e)
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewModel = ViewModelProvider(requireActivity()).get(ImageViewModel::class.java)

        // Restore the image if available
        imageViewModel.selectedImageBitmap?.let {
            handleSelectedImage(it)
        }

        binding.btnUpload.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PermissionChecker.PERMISSION_GRANTED) {
                openGallery()
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
    }
}
