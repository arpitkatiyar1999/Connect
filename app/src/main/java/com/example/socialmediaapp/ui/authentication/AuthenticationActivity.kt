package com.example.socialmediaapp.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ui.homescreen.MainActivity
import com.google.firebase.auth.FirebaseAuth



class AuthenticationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        supportActionBar?.hide()
        initAll()
        checkSignin()
        hideStatusBar()
    }

    private fun checkSignin() {
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }
    }

    private fun initAll() {
        auth = FirebaseAuth.getInstance()
    }

    @Suppress("DEPRECATION")
    private fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        supportActionBar?.hide()
    }
}