package com.example.socialmediaapp.ui.homescreen.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.socialmediaapp.R
import com.example.socialmediaapp.adapters.SearchUserAdapter
import com.example.socialmediaapp.databinding.FragmentSearchBinding
import com.example.socialmediaapp.models.UserModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userList: ArrayList<UserModel>
    private lateinit var searchUserAdapter: SearchUserAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        initAll()
        return binding.root
    }

    private fun initAll() {
        auth = FirebaseAuth.getInstance()
        userList = ArrayList()
        searchUserAdapter = SearchUserAdapter(userList)
        database = FirebaseDatabase.getInstance("https://social-media-app-27247-default-rtdb.firebaseio.com/")
        database.reference.child("Users").addValueEventListener(valueEventListener)
        with(binding)
        {
            recyclerView.adapter = searchUserAdapter
        }
    }

    private val valueEventListener = object : ValueEventListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                for(user in snapshot.children)
                    userList.add(user.getValue(UserModel::class.java)!!)
                searchUserAdapter.notifyDataSetChanged()
            }
            else
                showSnackbar("Some Error Occurred")
        }
        override fun onCancelled(error: DatabaseError) {
            showSnackbar(error.toString())
        }

    }
    private fun showSnackbar(text:String)
    {
        Snackbar.make(binding.root, text,
            Snackbar.LENGTH_SHORT).show()
    }
}