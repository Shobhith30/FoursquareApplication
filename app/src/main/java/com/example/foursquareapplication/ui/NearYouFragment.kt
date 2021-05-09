package com.example.foursquareapplication.ui

import android.Manifest
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquareapplication.OnFavouriteCLickListener
import com.example.foursquareapplication.R
import com.example.foursquareapplication.adapter.PlaceAdapter
import com.example.foursquareapplication.databinding.FragmentNearYouBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.model.DataPlace
import com.example.foursquareapplication.viewmodel.FavouriteViewModel
import com.example.foursquareapplication.viewmodel.LocationViewModel
import com.example.foursquareapplication.viewmodel.PlaceViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng


class NearYouFragment : Fragment(), OnFavouriteCLickListener {
    private var myMap: GoogleMap? = null
    private var mapReady: Boolean = false
    private var location: Location? = null
    private lateinit var nearYouBinding: FragmentNearYouBinding
    private lateinit var placeViewModel: PlaceViewModel
    private val locationViewModel: LocationViewModel by activityViewModels()
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var favouriteViewModel: FavouriteViewModel
    private val pageData: MutableLiveData<PagedList<DataPlace>> = MutableLiveData()
    private lateinit var locationPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        nearYouBinding = FragmentNearYouBinding.inflate(inflater, container, false)
        val fm = childFragmentManager
        val supportMapFragment = SupportMapFragment.newInstance()
        fm.beginTransaction().replace(R.id.mapLayout, supportMapFragment).commit()
        locationPreferences = requireContext().getSharedPreferences(Constants.LAST_LOCATION,MODE_PRIVATE)
        supportMapFragment.getMapAsync {
            myMap = it
            mapReady = true
            enableMyLocationIfPermitted()
            setCurrentLocationOnMap()

        }


        favouriteViewModel =
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
                .create(FavouriteViewModel::class.java)
        nearYouBinding.locationProgress.visibility = View.VISIBLE
        setPlaceAdapter()
        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)
        setLastPlaceData()
        enableMyLocationIfPermitted()
        setCurrentLocationOnMap()
        locationViewModel.getLocation().observe(viewLifecycleOwner, {
            location = it
            saveLocation(it)
            enableMyLocationIfPermitted()
            setCurrentLocationOnMap()
            loadPlaceData(it)

        })
        return nearYouBinding.root
    }

    private fun setLastPlaceData() {
        val location  =getLastLocation()
        if(location!=null){
            loadPlaceData(location)
        }
    }

    private fun getLastLocation() : Location? {
        if(locationPreferences.contains(Constants.LATITUDE)){
            val latitude = locationPreferences.getString(Constants.LATITUDE,"")?.toDouble()
            val longitude = locationPreferences.getString(Constants.LONGITUDE,"")?.toDouble()
            val location = Location("")
            if (latitude != null && longitude!=null) {
                location.latitude = latitude
                location.longitude = longitude
            }
            return location
        }else
            return null
    }

    private fun saveLocation(location : Location) {
        val locationEditor = locationPreferences.edit()
        locationEditor.putString(Constants.LATITUDE,location.latitude.toString())
        locationEditor.putString(Constants.LONGITUDE,location.longitude.toString())
        locationEditor.apply()
    }

    private fun setPlaceAdapter() {
        placeAdapter = PlaceAdapter(requireActivity())
        nearYouBinding.placeRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        nearYouBinding.placeRecyclerview.isNestedScrollingEnabled = false
        nearYouBinding.placeRecyclerview.adapter = placeAdapter
        placeAdapter.setClickListener(this)
    }

    private fun setCurrentLocationOnMap() {
        if (mapReady && location != null) {
            myMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location!!.latitude,
                        location!!.longitude
                    ), 15.0f
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()

        getData()

    }


    private fun loadPlaceData(location: Location) {
        nearYouBinding.locationProgress.visibility = View.GONE
        if (arguments?.getString("key") != null) {
            val type = arguments?.getString("key").toString()
            if (type == "nearBy") {
                nearYouBinding.mapLayout.visibility = View.VISIBLE
            }

            nearYouBinding.locationProgress.visibility = View.VISIBLE
            getFavourite()
            placeViewModel.getPlaceDetails(type, location.latitude, location.longitude)
                ?.observe(viewLifecycleOwner, {
                    if (it != null) {
                        if (it.size > 0) {
                            if (it.get(0)?.getPlace() != null) {
                                pageData.value = it

                            }
                        }
                    }
                    nearYouBinding.locationProgress.visibility = View.GONE

                })
        }
    }

    private fun getData() {
        pageData.observe(this, {
            if (it != null) {
                if (it.size > 0) {
                    if (it.get(0)?.getPlace() != null) {
                        setPlaceAdapter()
                        placeAdapter.submitList(it)
                    }
                }
            }

        })
    }


    private fun getFavourite() {
        val favouriteViewModel =
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
                .create(FavouriteViewModel::class.java)
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            Constants.USER_PREFERENCE,
            Context.MODE_PRIVATE
        )
        val userId = sharedPreferences.getString(Constants.USER_ID, "")
        var token = sharedPreferences.getString(Constants.USER_TOKEN, "")
        if(sharedPreferences.contains(Constants.USER_ID)) {
            if (userId != null && token != null) {
                val userToken = "Bearer $token"
                val pageNumber = Constants.FAV_PAGE_NUMBER
                val pageSize = Constants.FAV_PAGE_SIZE
                favouriteViewModel.getFavouriteData(userId.toInt(), pageNumber, pageSize, userToken)
                    .observe(viewLifecycleOwner, {
                        if (it != null) {
                            if (it.getStatus() == Constants.STATUS_OK) {
                                placeAdapter.favouriteData = it.getData()
                                placeAdapter.notifyDataSetChanged()

                            }
                        }
                    })
            }
        }
    }


    private fun enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                Constants.LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            if (myMap != null) {

                myMap?.isMyLocationEnabled = true

            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    enableMyLocationIfPermitted()
                }
                return
            }
        }
    }

    override fun onFavouriteClick(isFav: Boolean, id: Int?) {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(
            Constants.USER_PREFERENCE,
            Context.MODE_PRIVATE
        )
        val userId = sharedPreferences.getString(Constants.USER_ID, "")
        var token = sharedPreferences.getString(Constants.USER_TOKEN, "")
        if (isFav) {
            if (token != null && userId != null)
                addFavourite(id, userId, token)
            else
                Toast.makeText(context, "Please Login", Toast.LENGTH_LONG).show()
        } else {
            if (token != null && userId != null)
                removeFavourite(id, userId, token)
            else
                Toast.makeText(context, "Please Login", Toast.LENGTH_LONG).show()
        }

    }

    private fun addFavourite(item: Int?, userId: String, token: String) {
        if (item != null) {
            val token = "Bearer $token"
            val placeId = item.toString()
            val favourite = hashMapOf("userId" to userId, "placeId" to placeId)
            favouriteViewModel.addToFavourite(token, favourite).observe(this, {
                if (it != null) {
                    if (it.getStatus() == Constants.STATUS_OK) {

                        Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun removeFavourite(item: Int?, userId: String, token: String) {
        if (item != null) {
            val token = "Bearer $token"
            val placeId = item.toString()
            val favourite = hashMapOf("userId" to userId, "placeId" to placeId)
            favouriteViewModel.deleteFavourite(token, favourite).observe(this, {
                if (it != null) {
                    if (it.getStatus() == Constants.STATUS_OK) {
                        Toast.makeText(
                            context,
                            "Removed from favourite",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            })
        }
    }
}