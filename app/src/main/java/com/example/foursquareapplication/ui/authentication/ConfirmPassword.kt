package com.example.foursquareapplication.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityConfrimPasswordBinding
import com.example.foursquareapplication.databinding.ActivityForgotPasswordBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.Status
import com.example.foursquareapplication.viewmodel.AuthenticationViewModel

class ConfirmPassword : AppCompatActivity() {

    private  lateinit var confirmPasswordBinding: ActivityConfrimPasswordBinding
    private lateinit var authenticationViewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        confirmPasswordBinding = ActivityConfrimPasswordBinding.inflate(layoutInflater)
        setContentView(confirmPasswordBinding.root)

        authenticationViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AuthenticationViewModel::class.java)

        setConfirmPassword()
    }

    private fun setConfirmPassword() {

        confirmPasswordBinding.submit.setOnClickListener {
            val email = intent.getStringExtra("email").toString()
            val password = confirmPasswordBinding.password.text.toString().trim()
            val reEnteredPassword =
                confirmPasswordBinding.confrimPasswordValue.text.toString().trim()

            if (validateUserInputs(password, reEnteredPassword)) {
                val user = hashMapOf("email" to email, "password" to password)
                authenticationViewModel.confirmPassword(user).observe(this) {
                    when (it.status) {
                        Status.LOADING -> showProgressBar()
                        Status.SUCCESS -> {
                            val response = it.data
                            Toast.makeText(
                                applicationContext,
                                response?.getMessage(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        Status.ERROR -> {
                            Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT)
                                .show()
                            hideProgressBar()
                        }
                    }

                }
            }
        }
    }

    private fun showProgressBar() {
        confirmPasswordBinding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar(){
        confirmPasswordBinding.progressBar.visibility = View.GONE
    }


    private fun validateUserInputs( password: String, rePassword: String): Boolean {
        var isValid = false
        when{
            password.isEmpty()->{
                confirmPasswordBinding.password.error = "Enter Password"
                confirmPasswordBinding.password.requestFocus()

            }
            rePassword.isEmpty() ->{
                confirmPasswordBinding.confrimPasswordValue.error = "Re Enter Password"
                confirmPasswordBinding.confrimPasswordValue.requestFocus()

            }
            password!=rePassword ->{
                confirmPasswordBinding.confrimPasswordValue.error = "Password Doesn't match"
                confirmPasswordBinding.confrimPasswordValue.requestFocus()

            }
            else-> isValid = true
        }
        return isValid


    }
}