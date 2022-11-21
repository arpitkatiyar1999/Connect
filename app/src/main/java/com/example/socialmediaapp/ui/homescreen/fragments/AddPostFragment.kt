package com.example.socialmediaapp.ui.homescreen.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.UserHandle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.FragmentAddPostBinding
import com.example.socialmediaapp.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class AddPostFragment : Fragment() {
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!
    private var isImageSelected=false
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        _binding = FragmentAddPostBinding.inflate(layoutInflater, container, false)
        initAll()
        return binding.root
    }
    private fun initAll() {
        auth = FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance("https://social-media-app-27247-default-rtdb.firebaseio.com/")
        database.reference.child("Users").child(auth.uid!!).addListenerForSingleValueEvent(valueEventListener)
        with(binding)
        {
            postDescEdit.addTextChangedListener(textWatcher)
            selectImg.setOnClickListener { openGallery() }
        }
    }
    private fun openGallery()
    {
        val intent= Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if(data!=null && data.data!=null)
            {
                val uri=data.data
                isImageSelected=true
                with(binding)
                {
                    if(postImg.visibility!=View.VISIBLE)
                        postImg.visibility=View.VISIBLE
                    postImg.setImageURI(uri)
                }
            }
        }
        val count=binding.postDescEdit.text.toString().length
        binding.postBtn.isEnabled = (count != 0) || isImageSelected
    }
    private val valueEventListener=object:ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
           if(snapshot.exists())
           {
               val user=snapshot.getValue(UserModel::class.java)
               with(binding)
               {
                   Picasso.get().load(user?.profilePhoto).placeholder(R.drawable.img_placeholder).into(userProfileImg)
                   userNameTxt.text=user?.name
                   userProfessionTxt.text=user?.profession
               }

           }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            with(binding)
            {
                postBtn.isEnabled = (count != 0) || isImageSelected
            }

        }
    }
}