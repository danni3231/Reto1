package com.dannir.reto1.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.dannir.reto1.activities.LoginActivity
import com.dannir.reto1.databinding.FragmentProfileBinding
import com.dannir.reto1.model.User
import com.dannir.reto1.model.mvvm.ProfileViewModel
import com.dannir.reto1.model.utils.UtilDomi
import java.io.File
import java.util.*


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var file: File
    private var uriPath = ""
    private var currentUser: User? = null

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ::onCamaraResult
        )

        val galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ::onGalleryResult
        )

        profileViewModel.user.observe(viewLifecycleOwner) {

            currentUser = it

            binding.profileUsernameTF.editText?.setText(it.name)

            if (it.profileImg.isNotEmpty()){
                uriPath = it.profileImg
                val bitmap = BitmapFactory.decodeFile(it.profileImg)

                val thumbnail =
                    Bitmap.createScaledBitmap(
                        bitmap,
                        bitmap.width / 4,
                        bitmap.height / 4,
                        true
                    )
                binding.profilePreviewImg.setImageBitmap(thumbnail)
            }
        }

        binding.profileOpenCameraBtn.setOnClickListener {
            val fileID = UUID.randomUUID().toString()
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file = File("${requireContext().getExternalFilesDir(null)}/post_$fileID")
            val uri = FileProvider.getUriForFile(requireContext(), "com.dannir.reto1", file)
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            cameraLauncher.launch(i)
        }

        binding.profileOpenGalleryBtn.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/"
            galleryLauncher.launch(i)
        }

        binding.profileSaveBtn.setOnClickListener {
            val newProfileImg = binding.profileUsernameTF.editText?.text!!.toString()
            if (validateData(newProfileImg,uriPath)){
                updateUser(newProfileImg,uriPath)
            }
        }

        binding.profileLogoutBtn.setOnClickListener {
            logOut()
        }

        return binding.root
    }

    private fun logOut() {
        profileViewModel.logOut()
        val intent = Intent(context, LoginActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    private fun updateUser(newUsername: String, newProfileImg: String) {
        currentUser?.name = newUsername
        currentUser?.profileImg = newProfileImg

        profileViewModel.updateUser(currentUser!!)

        Toast.makeText(context,"Usuario actualizado", Toast.LENGTH_SHORT).show()
    }

    private fun validateData(newUsername: String, newProfileImg: String): Boolean{
        return when {
            newUsername.isEmpty() || newUsername.contentEquals(currentUser?.name) -> {
                Toast.makeText(requireContext(), "Cambia el nombre de usuario", Toast.LENGTH_SHORT).show()
                false
            }
            newProfileImg.isEmpty() || newProfileImg.contentEquals(currentUser?.profileImg) -> {
                Toast.makeText(requireContext(), "Cambia la foto de perfil", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

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

            binding.profilePreviewImg.setImageBitmap(scaledBitmap)
        }

    }

    private fun onGalleryResult(activityResult: ActivityResult) {

        if(activityResult.resultCode == Activity.RESULT_OK){
            val uri = activityResult.data?.data
            val path = UtilDomi.getPath(requireContext(), uri!!)
            uriPath = path!!
            val bitmap = BitmapFactory.decodeFile(path)

            binding.profilePreviewImg.setImageBitmap(bitmap)
        }

    }


}