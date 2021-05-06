package com.example.foursquareapplication.helper

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocationService {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            CoroutineScope(Dispatchers.Main).launch {
                location = locationResult.lastLocation
                onSuccess?.let { it(location) }
            }

        }
    }
    private var onSuccess: ((Location) -> Unit)? = null
    private lateinit var onError: () -> Unit
    lateinit var location: Location

    fun init(activity: Activity) {
        fusedLocationProviderClient = FusedLocationProviderClient(activity)
        locationRequest = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(1000).setFastestInterval(1000).setNumUpdates(1)
    }

    private fun checkLocationStatusAndGetLocation(activity: Activity) {
        CoroutineScope(Dispatchers.IO).launch {
            when {
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> LocationServices.getSettingsClient(activity).checkLocationSettings(LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setAlwaysShow(true).build()).addOnCompleteListener { task ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            task.getResult(ApiException::class.java)
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                        } catch (exception: ApiException) {
                            when (exception.statusCode) {
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                    try {
                                        (exception as ResolvableApiException).startResolutionForResult(activity, 7025)
                                    } catch (ex: Exception) {

                                    }
                                }
                                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                                }
                            }
                        }
                    }
                }
                //ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) -> activity.runOnUiThread {

                //}
                else -> ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 7024)
            }
        }
    }

    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, grantResults: IntArray) {
        if (requestCode == 7024) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationStatusAndGetLocation(activity)
            } else {
                onError()
            }
        }
    }

    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int) {
        if (requestCode == 7025) {
            if (resultCode == Activity.RESULT_OK) {
                checkLocationStatusAndGetLocation(activity)
            } else {
                onError()
            }
        }
    }

    fun getLocation(activity: Activity, onSuccess: (Any?) -> Unit, onError: () -> Unit) {
        this.onSuccess = onSuccess
        this.onError = onError
        checkLocationStatusAndGetLocation(activity)
    }

}