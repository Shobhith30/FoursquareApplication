package com.example.foursquareapplication.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityForgotPasswordBinding
import com.example.foursquareapplication.helper.Constants
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
            authenticationViewModel.generateOtp(email).observe(this, {
                if (it.getStatus() == Constants.STATUS_OK) {
                    Toast.makeText(applicationContext, it.getMessage(), Toast.LENGTH_SHORT).show()
                    setOtp()
                } else {
                    Toast.makeText(applicationContext, it.getMessage(), Toast.LENGTH_SHORT).show()
                }
            })
        }
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
        //Toast.makeText(applicationContext, "$mail $enteredOtp", Toast.LENGTH_SHORT).show()
        authenticationViewModel.validateOtp(otp).observe(this,{
            if (it.getStatus() == Constants.STATUS_OK) {
                Toast.makeText(applicationContext, it.getMessage(), Toast.LENGTH_SHORT).show()
                val intent = Intent(this,ConfirmPassword::class.java)
                intent.putExtra("email" , mail)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext, it.getMessage(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}