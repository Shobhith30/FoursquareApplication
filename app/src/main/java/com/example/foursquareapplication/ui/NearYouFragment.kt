package com.example.foursquareapplication.ui

import android.Manifest
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquareapplication.R
import com.example.foursquareapplication.adapter.PlaceAdapter
import com.example.foursquareapplication.adapter.ReviewAdapter
import com.example.foursquareapplication.databinding.FragmentNearYouBinding
import com.example.foursquareapplication.helper.Constants
import com.example.foursquareapplication.viewmodel.LocationViewModel
import com.example.foursquareapplication.viewmodel.PlaceViewModel
import com.example.foursquareapplication.viewmodel.ReviewViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng


class NearYouFragment : Fragment() {
    private  var myMap : GoogleMap? = null
    private var mapReady : Boolean = false
    private var location : Location? = null
    private lateinit var nearYouBinding : FragmentNearYouBinding
    private lateinit var placeViewModel: PlaceViewModel
    private val locationViewModel  :LocationViewModel by activityViewModels()
    private lateinit var placeAdapter : PlaceAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        nearYouBinding = FragmentNearYouBinding.inflate(inflater,container,false)
        nearYouBinding.locationProgress.visibility = View.VISIBLE
        val fm = childFragmentManager
        val supportMapFragment = SupportMapFragment.newInstance()
        fm.beginTransaction().replace(R.id.mapLayout, supportMapFragment).commit()
        supportMapFragment.getMapAsync{
            myMap = it
            mapReady = true
            enableMyLocationIfPermitted()
            setCurrentLocationOnMap()

        }

        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)
        locationViewModel.getLocation().observe(viewLifecycleOwner, {
            location = it
            enableMyLocationIfPermitted()
            setCurrentLocationOnMap()
            loadPlaceData(it)
            //Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
        })
        return  nearYouBinding.root
    }

    private fun setCurrentLocationOnMap() {
        if(mapReady && location!=null){
            myMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude,location!!.longitude),15.0f))
        }
    }


    private fun loadPlaceData(location: Location) {
        nearYouBinding.locationProgress.visibility = View.GONE
        if(arguments?.getString("key")!=null) {
            val type = arguments?.getString("key").toString()
            if(type == "nearBy"){
                nearYouBinding.mapLayout.visibility = View.VISIBLE
            }

            placeAdapter = PlaceAdapter(requireContext())
            nearYouBinding.placeRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            nearYouBinding.placeRecyclerview.isNestedScrollingEnabled = false
            nearYouBinding.placeRecyclerview.adapter = placeAdapter
            placeViewModel.getPlaceDetails(type, location.latitude, location.longitude)
                ?.observe(viewLifecycleOwner, {
                    if (it != null) {
                        if (it.size>0) {
                            if(it.get(0)?.getPlace() != null)
                            placeAdapter.submitList(it)
                        }
                    }

                })
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
}