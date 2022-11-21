package com.example.socialmediaapp.ui.authentication.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.FragmentSigninBinding
import com.example.socialmediaapp.ui.authentication.AuthenticationActivity
import com.example.socialmediaapp.ui.homescreen.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class SigninFragment : Fragment() {
    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSigninBinding.inflate(layoutInflater, container, false)
        initAll()
        return binding.root
    }
    private fun initAll() {
        auth = FirebaseAuth.getInstance()
        with(binding)
        {
            registerTxt.setOnClickListener { Navigation.findNavController(registerTxt).navigate(R.id.action_signinFragment_to_signupFragment) }
            loginBtn.setOnClickListener { validateAllData() }
            forgotPasswordTxt.setOnClickListener { manageForgotPassword() }
        }
    }
    private fun validateAllData()
    {
        with(binding)
        {
            val email=emailEdit.text.toString()
            val password=passwordEdit.text.toString()
            if(email.isBlank())
            {
                emailContainer.requestFocus()
                showSnackbar("Please Enter Your Email")
                return
            }
            if(password.isBlank())
            {
                showSnackbar("Please Enter Your Password")
                passwordContainer.requestFocus()
                return
            }
            if(password.length<6)
            {
                showSnackbar("Password should be of atleast 6 characters")
                passwordContainer.requestFocus()
                return
            }
            loginUser(email,password)
        }
    }

    private fun loginUser(email: String, password: String) {
        showProgressBar()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    hideProgressBar()
                    showSnackbar("Logged in Successfully")
                    (activity as AuthenticationActivity).startActivity(Intent(requireActivity(), MainActivity::class.java))
                    (activity as AuthenticationActivity).finish()
                } else {
                    Log.e("abc",task.exception.toString())
                    when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            when (val error=task.exception!!.toString().substringAfter(':').trim()) {
                                "The email address is badly formatted." -> showSnackbar("Incorrect Email Address")
                                "The password is invalid or the user does not have a password." -> showSnackbar("Incorrect Password")
                                else -> showSnackbar(error)
                            }
                        }
                        is FirebaseAuthInvalidUserException -> showSnackbar("Unregistered Email address")
                        else -> showSnackbar( task.exception!!.toString().substringAfter(':'))
                    }
                    hideProgressBar()
                }
            }
    }
    private fun manageForgotPassword()
    {
        var email:String
        with(binding)
        {
            email=emailEdit.text.toString()
            if(email.isBlank())
            {
                emailContainer.requestFocus()
                showSnackbar("Please Enter Your Email")
                return
            }
        }
        showProgressBar()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showSnackbar("Password reset link has been sent to your registered email address")
                    hideProgressBar()
                }
                else
                    showSnackbar( task.exception!!.toString().substringAfter(':'))
            }
    }
    private fun showSnackbar(text:String)
    {
        Snackbar.make(binding.root, text,
            Snackbar.LENGTH_SHORT).show()
    } private fun showProgressBar() {
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
}