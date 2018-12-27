package com.fusecoords.drivedroid.Customer

import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.fusecoords.drivedroid.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import java.lang.Exception


class AddressLocation : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null

    companion object {
        var Address = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_location)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        var geocoder = Geocoder(this)

        var p1 = null;

        try {
            var address = geocoder.getFromLocationName(Address, 5);

            var location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            val sydney = LatLng(location.getLatitude(), location.getLongitude())
            mMap!!.addMarker(MarkerOptions().position(sydney).snippet(Address))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))


        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    // Add a marker in Sydney, Australia, and move the camera.


}
