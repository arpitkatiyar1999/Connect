package com.example.socialmediaapp.ui.homescreen.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.FragmentProfileBinding
import com.example.socialmediaapp.models.UserModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var storage:FirebaseStorage
    private lateinit var database:FirebaseDatabase
    private var openGalleryFromWhere=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        initAll()
        return binding.root
    }

    private fun initAll() {
        auth= FirebaseAuth.getInstance()
        storage= FirebaseStorage.getInstance("gs://social-media-app-27247.appspot.com")
        database= FirebaseDatabase.getInstance("https://social-media-app-27247-default-rtdb.firebaseio.com/")
        database.reference.child("Users").child(auth.uid!!).addListenerForSingleValueEvent(valueEventListener)
       with(binding)
       {
           coverCameraImg.setOnClickListener{
               openGalleryFromWhere=1
               openGallery()
           }
           profileCameraImg.setOnClickListener {
               openGalleryFromWhere=2
               openGallery()
           }
       }
    }

    private fun openGallery()
    {
        val intent=Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }
    private fun updateCoverPhoto(data:Intent)
    {
        showProgressBar()
        val uri=data.data
        val storageReference=storage.reference.child("cover_photo").child(auth.uid!!)
        storageReference.putFile(uri!!).addOnCompleteListener{ task ->
            if(task.isSuccessful)
            {
               storageReference.downloadUrl.addOnSuccessListener {
                   database.reference.child("Users").child(auth.uid!!).child("coverPhoto").setValue(it.toString())
                   binding.coverPhotoImg.setImageURI(uri)
                   showSnackbar("Cover Photo Updated Successfully")
                   hideProgressBar()
               }.addOnFailureListener {
                   showSnackbar(task.exception.toString().substringAfter(':'))
                   hideProgressBar()
               }
            }
            else
                showSnackbar(task.exception.toString().substringAfter(':'))
            hideProgressBar()

        }
    }
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if(data!=null && data.data!=null)
            {
                if(openGalleryFromWhere==1)
                updateCoverPhoto(data)
                if(openGalleryFromWhere==2)
                    updateProfilePhoto(data)
            }
        }
    }

    private fun updateProfilePhoto(data: Intent) {
        showProgressBar()
        val uri = data.data
        val storageReference = storage.reference.child("profile_photo").child(auth.uid!!)
        storageReference.putFile(uri!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageReference.downloadUrl.addOnSuccessListener {
                    database.reference.child("Users").child(auth.uid!!).child("profilePhoto")
                        .setValue(it.toString())
                    binding.userProfileImg.setImageURI(uri)
                    showSnackbar("Profile Photo Updated Successfully")
                    hideProgressBar()
                }.addOnFailureListener {
                    showSnackbar(task.exception.toString().substringAfter(':'))
                    hideProgressBar()
                }
            } else
                showSnackbar(task.exception.toString().substringAfter(':'))
            hideProgressBar()
        }
    }
    private val valueEventListener=object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val user :UserModel?= snapshot.getValue(UserModel::class.java)
                if(user!=null){
                    with(binding)
                    {
                        userNameTv.text=user.name
                        userProfessionTv.text=user.profession
                        numOfFollowersTxt.text=user.followers
                        if(user.coverPhoto.isNotBlank())
                            Picasso.get().load(user.coverPhoto).placeholder(R.drawable.img_placeholder).into(coverPhotoImg)
                        else
                            coverPhotoImg.setImageResource(R.drawable.img_placeholder)
                        if(user.profilePhoto.isNotBlank())
                            Picasso.get().load(user.profilePhoto).placeholder(R.drawable.img_placeholder).into(userProfileImg)
                        else
                            userProfileImg.setImageResource(R.drawable.img_placeholder)
                    }
                }
                else
                    showSnackbar("Some Error Occurred")
                Log.e("abc",user.toString())
            }
        }

        override fun onCancelled(error: DatabaseError) {
            showSnackbar(error.toString())
        }
    }
        private fun showProgressBar() {
        with(binding)
        {
            if(progressBar.root.visibility!=View.VISIBLE)
                progressBar.root.visibility=View.VISIBLE
        }
    }
    private fun hideProgressBar() {
        with(binding)
        {
            if(progressBar.root.visibility!=View.GONE)
                progressBar.root.visibility=View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    private fun showSnackbar(text:String)
    {
        Snackbar.make(binding.root, text,
            Snackbar.LENGTH_SHORT).show()
    }
}