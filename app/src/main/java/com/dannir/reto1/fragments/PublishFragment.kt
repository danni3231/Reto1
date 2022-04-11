package com.dannir.reto1.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.dannir.reto1.R
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.text.set
import androidx.lifecycle.ViewModelProvider
import com.dannir.reto1.databinding.FragmentPublishBinding
import com.dannir.reto1.model.Post
import com.dannir.reto1.model.mvvm.HomeViewModel
import com.dannir.reto1.model.mvvm.PublishViewModel
import com.dannir.reto1.model.utils.UtilDomi
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PublishFragment : Fragment() {

    private var _binding: FragmentPublishBinding? = null
    private val binding get() = _binding!!

    private lateinit var publishViewModel: PublishViewModel
    private lateinit var file: File
    private var uriPath = ""

    override fun onResume() {
        super.onResume()
        val cities = resources.getStringArray(R.array.cities)
        val citiesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, cities)
        binding.publishCityDB.setAdapter(citiesAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PublishFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        publishViewModel = ViewModelProvider(this)[PublishViewModel::class.java]
        _binding = FragmentPublishBinding.inflate(inflater, container, false)

        val cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ::onCamaraResult
        )

        val galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ::onGalleryResult
        )

        binding.publishOpenCameraBtn.setOnClickListener {
            var fileID = UUID.randomUUID().toString()
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file = File("${requireContext().getExternalFilesDir(null)}/post_$fileID")
            val uri = FileProvider.getUriForFile(requireContext(), "com.dannir.reto1", file)
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            cameraLauncher.launch(i)
        }

        binding.publishOpenGalleryBtn.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/"
            galleryLauncher.launch(i)
        }

        binding.publishUploadBtn.setOnClickListener {

            val description = binding.publishDescriptionTF.editText?.text!!.toString()
            val city = binding.publishCityTF.editText?.text!!.toString()
            val date = Calendar.getInstance()

            if (validateData(description, city, uriPath)) {
                //upload
                val id = UUID.randomUUID().toString()
                val uid = publishViewModel.getUser().id

                uploadPost(id,uid, uriPath,description,city,date)
            }
        }

        return binding.root
    }

    //Personal Functions

    private fun onCamaraResult(activityResult: ActivityResult) {

        if(activityResult.resultCode == Activity.RESULT_OK){
            uriPath = file.path
            val bitmap = BitmapFactory.decodeFile(file.path)

            //procesamiento
            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap,
                bitmap.width / 5,
                bitmap.height / 5,
                true
            )

            binding.publishPreviewImg.setImageBitmap(scaledBitmap)
        }


    }

    private fun onGalleryResult(activityResult: ActivityResult) {
        if(activityResult.resultCode == Activity.RESULT_OK){
            val uri = activityResult.data?.data
            val path = UtilDomi.getPath(requireContext(), uri!!)
            uriPath = path!!
            val bitmap = BitmapFactory.decodeFile(path)

            binding.publishPreviewImg.setImageBitmap(bitmap)
        }
    }

    private fun uploadPost( id: String,uid: String, fileString: String,description: String,city: String,date: Calendar) {
        val post = Post(
            id,
            uid,
            fileString,
            description,
            city,
            date,
        )
        publishViewModel.uploadPost(post)

        cleanInputs()

        Toast.makeText(requireContext(), "Publicación creada", Toast.LENGTH_SHORT).show()
    }

    private fun cleanInputs(){
        binding.publishDescriptionTF.editText?.text?.clear()
        binding.publishCityTF.editText?.text?.clear()
        binding.publishPreviewImg.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.img_placeholder))
    }

    private fun validateData(description: String, city: String, uriPath: String): Boolean {
        return when {
            description.isEmpty() -> {
                Toast.makeText(requireContext(), "Agrega una descripción", Toast.LENGTH_SHORT).show()
                false
            }
            city.isEmpty() -> {
                Toast.makeText(requireContext(), "Agrega una ciudad", Toast.LENGTH_SHORT).show()
                false
            }
            uriPath.isEmpty() -> {
                Toast.makeText(requireContext(), "Elije o toma una foto", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

}