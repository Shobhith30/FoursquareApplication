package com.example.foursquareapplication

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foursquareapplication.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var detailsBinding: ActivityDetailsBinding
    var fav = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(detailsBinding.root)

        openRatingDialog()
        gotoPhotosScreen()
        gotoReviewScreen()
        gotoAddReviewScreen()
        setupActionBar()

    }

    private fun gotoAddReviewScreen() {
        detailsBinding.addReviewButton.setOnClickListener {
            val addReviewIntent = Intent(this, AddReviewActivity::class.java)
            startActivity(addReviewIntent)
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(detailsBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        detailsBinding.toolbar.setNavigationIcon(R.drawable.back_icon)
    }

    private fun gotoReviewScreen() {
        detailsBinding.toReviewScreen.setOnClickListener {
            val reviewIntent = Intent(this, ReviewActivity::class.java)
            startActivity(reviewIntent)
        }
    }

    private fun gotoPhotosScreen() {
        detailsBinding.toPhotoScreen.setOnClickListener {
            val photosIntent = Intent(this, PhotosActivity::class.java)
            startActivity(photosIntent)
        }
    }

    private fun openRatingDialog() {
        detailsBinding.toRatingScreen.setOnClickListener {
            val ratingDialog =
                layoutInflater.inflate(R.layout.rating_dialog, detailsBinding.rootView, false)
            val alertDialog = AlertDialog.Builder(this)
                .setView(ratingDialog)
                .setCancelable(false)
                .create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            alertDialog.show()
            ratingDialog.findViewById<ImageView>(R.id.close_dialog).setOnClickListener {
                alertDialog.cancel()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            R.id.fav_not_selected -> {
                fav = true
                invalidateOptionsMenu()
            }
            R.id.fav_selected -> {
                fav = false
                invalidateOptionsMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (fav) {
            menu?.findItem(R.id.fav_selected)?.setVisible(true)
            menu?.findItem(R.id.fav_not_selected)?.setVisible(false)
        } else {
            menu?.findItem(R.id.fav_not_selected)?.setVisible(true)
            menu?.findItem(R.id.fav_selected)?.setVisible(false)
        }


        return super.onPrepareOptionsMenu(menu)
    }
}