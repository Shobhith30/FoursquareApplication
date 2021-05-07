package com.example.foursquareapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.foursquareapplication.R
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.ui.authentication.SignInActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        checkUserLoggedIn()


    }

    private fun checkUserLoggedIn() {
        val sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
        if(sharedPreferences.contains(Constants.USER_ID) || sharedPreferences.contains(Constants.GUEST_USER))
            launchHomeScreen()
        else
            launchLoginScreen()
    }

    private fun launchLoginScreen() {
        lifecycleScope.launch {
            delay(2000)
            val intent = Intent(this@SplashScreenActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun launchHomeScreen() {
        lifecycleScope.launch {
            delay(1000)
            val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}