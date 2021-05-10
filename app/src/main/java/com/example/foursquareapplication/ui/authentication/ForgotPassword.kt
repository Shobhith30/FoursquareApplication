package com.example.foursquareapplication.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityForgotPasswordBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.Status
import com.example.foursquareapplication.viewmodel.AuthenticationViewModel

class ForgotPassword : AppCompatActivity() {

    private  lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding
    private lateinit var authenticationViewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(forgotPasswordBinding.root)
        authenticationViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            AuthenticationViewModel::class.java)

        setGetInListener()
        setResetOtpListener()

    }


    private fun setResetOtpListener() {
        forgotPasswordBinding.resendOtp.setOnClickListener {
            val mail = intent.getStringExtra("email").toString()
            val email = hashMapOf("email" to mail)
            authenticationViewModel.generateOtp(email).observe(this){
                when(it.status){
                    Status.LOADING ->{showProgressBar()}
                    Status.SUCCESS ->{
                        val response = it.data
                        Toast.makeText(applicationContext, response?.getMessage(), Toast.LENGTH_SHORT).show()
                        hideProgressBar()
                        setOtp()
                    }
                    Status.ERROR -> {
                        hideProgressBar()
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        forgotPasswordBinding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar(){
        forgotPasswordBinding.progressBar.visibility = View.GONE
    }

    private fun setGetInListener() {
        forgotPasswordBinding.getInButton.setOnClickListener {

            setOtp()
        }
    }

    private fun setOtp() {
        val mail = intent.getStringExtra("email").toString()
        val enteredOtp = forgotPasswordBinding.otp.text.toString()
        val otp = hashMapOf("email" to mail,"otpNum" to enteredOtp)
        authenticationViewModel.validateOtp(otp).observe(this){
            when(it.status){
                Status.LOADING -> showProgressBar()
                Status.SUCCESS ->{
                    val response = it.data
                    Toast.makeText(applicationContext, response?.getMessage(), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,ConfirmPassword::class.java)
                    intent.putExtra("email" , mail)
                    startActivity(intent)
                    finish()

                }
                Status.ERROR -> {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    hideProgressBar()
                }
            }
        }

    }
}