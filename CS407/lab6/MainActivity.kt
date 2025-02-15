package com.cs407.lab6

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync{googleMap:GoogleMap ->
            mMap = googleMap

            //Bascom Hill Location
            val mDestinationLatLng = LatLng(43.0753, -89.4034)
            val destinationName = "Bascom Hall"
            setLocationMarker(mDestinationLatLng, destinationName)
        }
    }

    private fun setLocationMarker(mDestinationLatLng: LatLng, destinationName: String){
        val markerOptions = MarkerOptions().position(mDestinationLatLng).title(destinationName)
        mMap.addMarker(markerOptions)

        //camera move
        //zoom -> zoom level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDestinationLatLng, 15f))
    }
}