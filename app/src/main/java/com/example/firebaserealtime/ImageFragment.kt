package com.example.firebaserealtime

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.firebaserealtime.databinding.FragmentImageBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.security.Permission
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var uri: Uri? = null
    val binding :FragmentImageBinding by lazy {
        FragmentImageBinding.inflate(layoutInflater)
    }
    private  val TAG = "ImageUploadFragment"

    var imagesPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            pickImage.launch("image/*")
        }
    }

    var pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        binding.imageView.setImageURI(it)
        uri = it
    }

    var permission = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }else{
        android.Manifest.permission.READ_MEDIA_IMAGES
    }

    var storageRef = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this).load("https://goo.gl/gEgYUd").into(binding.imageView)

        binding.btnSelected.setOnClickListener {
            imagesPermission.launch("image/*")

        }
        binding.btnUploaded.setOnClickListener {
            uri?.let {
                storageRef.getReference(Calendar.getInstance().timeInMillis.toString())
                    .putFile(it)
                    .addOnSuccessListener {
                        print("in success")
                    }
                    .addOnFailureListener{
                        Log.e(TAG, "failure $it")
                    }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ImageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}