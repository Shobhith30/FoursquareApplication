package com.example.foursquareapplication.ui

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foursquareapplication.R
import com.example.foursquareapplication.databinding.ActivityDetailsBinding
import com.example.foursquareapplication.helper.ChangeRatingColor
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.model.Place
import com.example.foursquareapplication.viewmodel.ReviewViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var detailsBinding: ActivityDetailsBinding
    var fav = true
    private var isLoggedIn = false
    private var placeResponse: Place? = null
    private lateinit var reviewViewModel: ReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(detailsBinding.root)

        reviewViewModel = ViewModelProvider.AndroidViewModelFactory(application)
                .create(ReviewViewModel::class.java)
        placeResponse = intent?.getParcelableExtra(Constants.PLACE_RESPOSNE)
        val sharedPreferences = getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
        isLoggedIn = sharedPreferences.contains(Constants.USER_ID)
        addGoogleMap()
        loadDataToViews(placeResponse)
        openRatingDialog()
        gotoPhotosScreen()
        gotoReviewScreen()
        gotoAddReviewScreen()
        setupActionBar()

    }

    private fun addGoogleMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun loadDataToViews(placeResponse: Place?) {
        if (placeResponse != null) {
            val placeData = placeResponse
            detailsBinding.overview.text =
                    placeData.getOverview()
            detailsBinding.rating.rating =
                    placeData.getOverallRating() / 2
            detailsBinding.address.text = placeData.getAddress()
            detailsBinding.phone.text = placeData.getPhone().toString()
            //detailsBinding.distance.text = String.format("%.1f Km", placeResponse.getDistance())
            Glide.with(this).load(placeData.getImage())
                    .placeholder(R.drawable.loading).into(detailsBinding.placeImage)
            val placeTypesList = arrayListOf<String>()
            for (type in placeData.getPlaceType()) {
                placeTypesList.add(type.getCategoryName())
            }
            val placeType = placeTypesList.joinToString(",")
            detailsBinding.placeType.text = placeType
        }
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
        detailsBinding.toolbarTitle.text = placeResponse?.getName()
        detailsBinding.toolbar.let {
            it.setNavigationIcon(R.drawable.back_icon)
            it.setNavigationOnClickListener {
                onBackPressed()
            }
        }

    }

    private fun gotoReviewScreen() {
        detailsBinding.toReviewScreen.setOnClickListener {
            if(!isLoggedIn){
                Toast.makeText(applicationContext, "Please Login to Add Review", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val reviewIntent = Intent(this, ReviewActivity::class.java)
            val placeId = placeResponse?.getPlaceId()
            reviewIntent.putExtra(Constants.PLACE_ID, placeId)
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
            val ratingValue = placeResponse?.getOverallRating()
            if (ratingValue != null) {
                val rating = ratingDialog.findViewById<TextView>(R.id.overall_rating)
                rating.setTextColor(ChangeRatingColor().getRatingColor(ratingValue))
                rating.text = ratingValue.toString()
            }
            val submitRating = ratingDialog.findViewById<TextView>(R.id.submit)
            val ratingBar = ratingDialog.findViewById<RatingBar>(R.id.rating_bar)
            submitRating.setOnClickListener {
                if(!isLoggedIn){
                    Toast.makeText(applicationContext, "Please Login to Add Review", Toast.LENGTH_SHORT).show()
                }else {
                    val userRating = ratingBar.rating.toInt()
                    submitUserRating(userRating)
                }

            }

        }
    }

    private fun submitUserRating(userRating : Int) {
        if (userRating > 0) {
            val sharedPreferences =
                    getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE)
            val userId = sharedPreferences.getString(Constants.USER_ID, "")
            val token = "Bearer ${sharedPreferences.getString(Constants.USER_TOKEN, "")}"
            val placeId = placeResponse?.getPlaceId()
            val rating = hashMapOf<String, String>(
                    "userId" to userId.toString(),
                    "placeId" to placeId.toString(),
                    "rating" to userRating.toString()
            )
//            reviewViewModel.addRating(token, rating).observe(this, {
//                if (it.getStatus() == Constants.STATUS_OK)
//                    Toast.makeText(
//                            applicationContext,
//                            "Thank you for your Feedback!",
//                            Toast.LENGTH_LONG
//                    ).show()
//                else
//                    Toast.makeText(applicationContext, it.getMessage(), Toast.LENGTH_LONG)
//                            .show()
//            })
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
            R.id.share -> {
                sharePlace()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sharePlace() {
        val sharePlaceIntent = Intent(Intent.ACTION_SEND)
        sharePlaceIntent.putExtra(Intent.EXTRA_TEXT, "place details")
        sharePlaceIntent.type = "plain/text"
        val chooser = Intent.createChooser(sharePlaceIntent, "Select App")
        startActivity(chooser)
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

    override fun onMapReady(maps: GoogleMap) {
        if (placeResponse != null) {

            val location = LatLng(placeResponse!!.getLatitude(), placeResponse!!.getLongitude())

            maps.addMarker(MarkerOptions().position(location))
            maps.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                    location.latitude,
                                    location.longitude
                            ), 16.0f
                    )
            )

        }

    }
}