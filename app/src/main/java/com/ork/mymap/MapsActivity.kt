package com.ork.mymap

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.util.Pair
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ork.mymap.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var lastLocation: Location

    lateinit var fusedLocationClient: FusedLocationProviderClient


    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        setUpMap()


    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            lastLocation = location

            binding.addPlace.setOnClickListener {
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12F))

                setPreference(lastLocation.latitude.toFloat(), lastLocation.longitude.toFloat())

            }

            val location = Location(LocationManager.GPS_PROVIDER)
            location.latitude =getSharePreference().first.toDouble()
            location.longitude = getSharePreference().second.toDouble()
            placeMarkerOnMap(LatLng(location.latitude, location.longitude))
        }
    }

    private fun getSharePreference():Pair<Float,Float>{
        val sharedPreferences = this.getSharedPreferences(Constants.AUTH_PREFS, MODE_PRIVATE)
        val locLat =  sharedPreferences.getFloat(Constants.LOC_LATITUDE, 2F)
        val locLong = sharedPreferences.getFloat(Constants.LOC_LONGITUDE, 1F)
        return Pair(locLat, locLong)
    }



    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val marketOption = MarkerOptions().position(currentLatLong)
        marketOption.title("$currentLatLong")
        mMap.addMarker(marketOption)

//
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }


    override fun onMarkerClick(p0: Marker) = true


    private fun setPreference(locLatitude: Float, locLongitude: Float) {
        val sharedPreferences = this.getSharedPreferences(Constants.AUTH_PREFS, MODE_PRIVATE)
        val shareEdit = sharedPreferences.edit()
        shareEdit.putFloat(Constants.LOC_LATITUDE, locLatitude)
        shareEdit.putFloat(Constants.LOC_LONGITUDE, locLongitude)
        shareEdit.apply()


    }


    // Log.d("here", "setUpMap: ${getLocation().first} ${getLocation().second}")

//            val location = Location(LocationManager.GPS_PROVIDER)
//            location.latitude = getLocation().first.toDouble()
//            location.longitude = getLocation().second.toDouble()
//            placeMarkerOnMap(LatLng(location.latitude, location.longitude))


//            val location = Location(LocationManager.GPS_PROVIDER)
//            location.latitude = getLocation().first.toDouble()
//            location.longitude = getLocation().second.toDouble()
//            placeMarkerOnMap(LatLng(location.latitude, location.longitude))

  //  private fun getLocation(): Pair<Float, Float> {
//        val sharedPreferences = this.getSharedPreferences(Constants.AUTH_PREFS, MODE_PRIVATE)
//        val locLat = sharedPreferences?.getFloat(Constants.LOC_LATITUDE, 0F)
//        val locLong = sharedPreferences?.getFloat(Constants.LOC_LONGITUDE, 1F)
//        return Pair(locLat!!, locLong!!)
//    }
//
//    private fun getLocation(): Pair<Float, Float> {
//        val sharedPreferences = this.getSharedPreferences(Constants.AUTH_PREFS, MODE_PRIVATE)
//        val locLat = sharedPreferences?.getFloat(Constants.LOC_LATITUDE, 0F)
//        val locLong = sharedPreferences?.getFloat(Constants.LOC_LONGITUDE, 1F)
//        return Pair(locLat!!, locLong!!)
//    }


}