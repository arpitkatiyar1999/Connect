package com.example.socialmediaapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaapp.databinding.ActivityAuthenticationBinding
import com.example.socialmediaapp.ui.homescreen.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


@Suppress("SameParameterValue")
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAuthenticationBinding
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initAll()
        checkSignin()
        hideStatusBar()
    }

    private fun checkSignin() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
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

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        showSnackbar("Press Back again to exit the app")
        Handler(Looper.getMainLooper()).postDelayed(
            {
                doubleBackToExitPressedOnce = false
            },
            2000)
    }
    private fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}