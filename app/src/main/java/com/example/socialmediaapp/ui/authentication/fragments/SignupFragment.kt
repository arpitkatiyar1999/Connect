package com.example.socialmediaapp.ui.authentication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.socialmediaapp.R
import com.example.socialmediaapp.databinding.FragmentSignupBinding
import com.example.socialmediaapp.models.UserModel
import com.example.socialmediaapp.ui.authentication.AuthenticationActivity
import com.example.socialmediaapp.ui.homescreen.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.FirebaseDatabase

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        initAll()
        return binding.root
    }
    private fun initAll() {
        auth = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance("https://social-media-app-27247-default-rtdb.firebaseio.com/")
        with(binding)
        {
            registerBtn.setOnClickListener { validateAllFields() }
            loginTxt.setOnClickListener {
                Navigation.findNavController(loginTxt)
                    .navigate(R.id.action_signupFragment_to_signinFragment)
            }
        }
    }
    private fun validateAllFields() {
        with(binding)
        {
            val name = nameEdit.text.toString()
            val profession = professionEdit.text.toString()
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            if (name.isBlank()) {
                showSnackbar("Please Enter Your Name")
                nameContainer.requestFocus()
                return
            }
            if (profession.isBlank()) {
                showSnackbar("Please Enter Your Profession")
                professionContainer.requestFocus()
                return
            }
            if (email.isBlank()) {
                showSnackbar("Please Enter Your Email")
                emailContainer.requestFocus()
                return
            }
            if (password.isBlank()) {
                showSnackbar("Please Enter Your Password")
                passwordContainer.requestFocus()
                return
            }
            if (password.length < 6) {
                showSnackbar("Password should be of atleast 6 characters")
                passwordContainer.requestFocus()
                return
            }
            authenticateUsingEmail(name, profession, email, password)
        }

    }

    private fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun authenticateUsingEmail(name: String, profession: String, email: String, password: String) {
        showProgressBar()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val userModel = UserModel(name, profession, email, "", "", "0", "0", "0", "")
                    val id = task.result.user?.uid
                    database.reference.child("Users").child(id!!).setValue(userModel)
                        .addOnCompleteListener {
                            if (task.isSuccessful) {
                                Toast.makeText(requireContext(),"Signed up Successfully",Toast.LENGTH_SHORT).show()
                                hideProgressBar()
                                (activity as AuthenticationActivity).startActivity(Intent(requireActivity(), MainActivity::class.java))
                                (activity as AuthenticationActivity).finish()
                            } else {
                                auth.currentUser?.delete()
                                showSnackbar("Some Error Occurred")
                                hideProgressBar()
                            }
                        }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException)
                        showSnackbar("Incorrect Email Address")
                    else
                        showSnackbar(task.exception!!.toString().substringAfter(':'))
                    hideProgressBar()
                }

            }
    }

    private fun showProgressBar() {
        with(binding)
        {
            if (progressBar.root.visibility != View.VISIBLE)
                progressBar.root.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        with(binding)
        {
            if (progressBar.root.visibility != View.GONE)
                progressBar.root.visibility = View.GONE
        }
    }
}