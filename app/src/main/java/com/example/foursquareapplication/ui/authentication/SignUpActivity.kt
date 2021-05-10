package com.example.foursquareapplication.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivitySignupBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.Status
import com.example.foursquareapplication.ui.HomeActivity
import com.example.foursquareapplication.viewmodel.AuthenticationViewModel
import kotlin.math.sign

class SignUpActivity :  AppCompatActivity() {

    private lateinit var signUpBinding : ActivitySignupBinding
    private lateinit var authenticationViewModel : AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)

        authenticationViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(AuthenticationViewModel::class.java)
        setSignUpOnClickListener()


    }

    private fun setSignUpOnClickListener() {
        signUpBinding.signupLogin.setOnClickListener {
           signUpUser()
        }
    }

    private fun signUpUser() {
        val email = signUpBinding.emailValue.text.toString().trim()
        val phone = signUpBinding.mobileValue.text.toString().trim()
        val password = signUpBinding.passwordValue.text.toString().trim()
        val rePassword = signUpBinding.signupConfrimPasswordValue.text.toString().trim()
        if(validateUserInputs(email,phone,password,rePassword)) {

            val user = hashMapOf("email" to email,"phone" to phone,"password" to password)
            authenticationViewModel.registerUser(user).observe(this, {

                if (it != null) {
                    if (it.getStatus() == 200) {

                        val loginUser = hashMapOf("email" to email, "password" to password)
                        authenticationViewModel.authenticateUser(loginUser).observe(this, {
                            when (it.status) {
                                Status.LOADING -> {
                                    showProgressBar()
                                }
                                Status.SUCCESS -> {
                                    val data = it.data
                                    if (data != null) {
                                        val sharedPreferences = getSharedPreferences(
                                            Constants.USER_PREFERENCE,
                                            MODE_PRIVATE
                                        )
                                        val sharedEditor = sharedPreferences.edit()
                                        val userId =
                                            data.getData().getUserData().getUserId().toString()
                                        val userName = data.getData().getUserData().getUserName()
                                        val userImage = data.getData().getUserData().getImage()
                                        val token = data.getData().getToken()
                                        sharedEditor.putString(Constants.USER_ID, userId)
                                        sharedEditor.putString(Constants.USER_NAME, userName)
                                        sharedEditor.putString(Constants.USER_IMAGE, userImage)
                                        sharedEditor.putString(Constants.USER_TOKEN, token)
                                        sharedEditor.remove(Constants.GUEST_USER)
                                        sharedEditor.apply()
                                        hideProgressBar()
                                        startActivity(Intent(this, HomeActivity::class.java))
                                        finish()
                                    }
                                }
                                Status.ERROR -> {
                                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                                    hideProgressBar()
                                }

                            }


                        })
                    }

                }
            })
        }
    }

    private fun showProgressBar() {
        signUpBinding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar(){
        signUpBinding.progressBar.visibility = View.GONE
    }

    private fun validateUserInputs(email: String, phone: String, password: String, rePassword: String): Boolean {
        var isValid = false
        when{
            email.isEmpty() ->{
                signUpBinding.emailValue.error = "Enter Email Address"
                signUpBinding.emailValue.requestFocus()

            }
            phone.isEmpty() ->{
                signUpBinding.mobileValue.error = "Enter Phone Number"
                signUpBinding.mobileValue.requestFocus()

            }
            phone.length != 10 -> {
                signUpBinding.mobileValue.error = "Enter Valid Phone Number"
                signUpBinding.mobileValue.requestFocus()
            }
            password.isEmpty()->{
                signUpBinding.passwordValue.error = "Enter Password"
                signUpBinding.passwordValue.requestFocus()

            }
            rePassword.isEmpty() ->{
                signUpBinding.signupConfrimPasswordValue.error = "Re Enter Password"
                signUpBinding.signupConfrimPasswordValue.requestFocus()

            }
            password!=rePassword ->{
                signUpBinding.signupConfrimPasswordValue.error = "Password Doesn't match"
                signUpBinding.signupConfrimPasswordValue.requestFocus()

            }
            else-> isValid = true
        }
        return isValid


    }
}