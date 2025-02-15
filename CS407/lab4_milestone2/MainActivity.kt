package com.cs407.lab4_milestone2

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location : Location->
            updateLocationInfo(location)
        }

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            startListening()
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
            val location : Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                updateLocationInfo(location)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening()
            }
        }
    }

    private fun startListening() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0L, 0f, locationListener
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun updateLocationInfo(location: Location) {
        val latText = findViewById<TextView>(R.id.lat)
        val lonText = findViewById<TextView>(R.id.lon)
        val altText = findViewById<TextView>(R.id.alt)
        val accText = findViewById<TextView>(R.id.acc)
        latText.text = "Latitude: ${location.latitude}"
        lonText.text = "Longitude: ${location.longitude}"
        altText.text = "Altitude: ${location.altitude}"
        accText.text = "Accuracy: ${location.accuracy}"

        val geocoder = Geocoder(applicationContext, Locale.getDefault())

        geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        ) { addresses ->
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                var addressText = getString(R.string.address)
                if (address.subThoroughfare != null) addressText += "${address.subThoroughfare} "
                if (address.thoroughfare != null) addressText += "${address.thoroughfare}\n"
                if (address.locality != null) addressText += "${address.locality}\n"
                if (address.postalCode != null) addressText += "${address.postalCode}\n"
                if (address.countryName != null) addressText += "${address.countryName}"
                findViewById<TextView>(R.id.addr).text = addressText
            } else {
                findViewById<TextView>(R.id.addr).text = "Could not find address"
            }
        }
    }

}