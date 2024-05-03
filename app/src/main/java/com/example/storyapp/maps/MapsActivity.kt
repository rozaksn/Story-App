package com.example.storyapp.maps

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.R
import com.example.storyapp.data.DataStoreViewModels
import com.example.storyapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocation:FusedLocationProviderClient
    private val dataStoreViewModel by viewModels<DataStoreViewModels>()
    //private val mapsViewModel by viewModels<MapsViewModel>()
    private val mapsViewModel:MapsViewModel by viewModels()
    private var token:String = ""
    //private lateinit var repo:StoryRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocation=LocationServices.getFusedLocationProviderClient(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

       getToken()

        //token=intent.getStringExtra(MainActivity.TOKEN)?:""
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        showMarkerLocation()
        getLastLocation()

    }

    private fun getToken(){
        lifecycleScope.launchWhenCreated {
            launch {
                mapsViewModel.getToken().collect{ myToken->
                    if (!myToken.isNullOrEmpty()) token=myToken

                }
            }
        }
    }
    private fun checkingPermission(permission:String):Boolean{
        return ContextCompat.checkSelfPermission(this,permission)==PackageManager
            .PERMISSION_GRANTED
    }

    private val requestPermission=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        permission ->
        when {
            permission[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                //fine location access granted
                // getLastLocation()
            }
            permission[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                //only coarse location granted
                getLastLocation()
            }
            else -> {}
        }
    }


    private fun showMarkerLocation(){
        lifecycleScope.launchWhenResumed {
            launch {
                mapsViewModel.allStoryMap(token).collect{mapResult->
                    mapResult.onSuccess { response->
                        response.listStory.forEach { storyItem->
                            if (storyItem.lat != null && storyItem.lon != null){
                                val latLon = LatLng(storyItem.lat,storyItem.lon)

                                mMap.addMarker(MarkerOptions().position(latLon).title("User: ${storyItem.name} || " + "Created at: ${storyItem.createdAt}")
                                    .snippet("Latitude: ${storyItem.lat}, Longitude: ${storyItem.lon}"))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getLastLocation(){
        if (checkingPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)&&
            checkingPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            showMarkerLocation()
        }else{
            requestPermission.launch(arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }
}