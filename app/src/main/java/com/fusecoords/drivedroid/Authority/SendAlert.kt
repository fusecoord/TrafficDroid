package com.fusecoords.drivedroid.Authority

import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fusecoords.drivedroid.CustomApp
import com.fusecoords.drivedroid.Customer.AddressLocation
import com.fusecoords.drivedroid.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_send_alert.*
import java.lang.Exception
import java.util.*

class SendAlert : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    var marker: Marker? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_alert)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        try {
            val sydney = LatLng(
                (application as CustomApp).currentLocation!!.latitude,
                (application as CustomApp).currentLocation!!.longitude
            )
            //mMap!!.addMarker(MarkerOptions().position(sydney).snippet(AddressLocation.Address))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        } catch (e: Exception) {
            e.printStackTrace()

        }
        sendAlert.setOnClickListener {

            var user = Alert()
            if (marker != null) {
                user.Address = marker!!.snippet
                user.Latitude = marker!!.position.latitude
                user.Longitude = marker!!.position.longitude
            }
            user.Note = note.text.toString()
            user.Date = (application as CustomApp).getDate(Date())// Date().toString()
            user.DateTime = (application as CustomApp).getDateTime(Date())// Date().toString()
            user.ReportedBy = FirebaseAuth.getInstance().currentUser!!.uid
            var mDatabase: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(Alert.DB_TABLE_LICENCE)
            var refer = mDatabase.push()
            mDatabase.child(refer.key!!)
                .setValue(user.getHashMap())
                .addOnCompleteListener(OnCompleteListener {
                    if (it.isSuccessful) {

                        Toast.makeText(
                            this@SendAlert, "Register update complete.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@SendAlert, "Register update failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
        mMap!!.setOnMapLongClickListener {

            var geocoder = Geocoder(this)

            var p1 = null;

            try {
                var result = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                if (marker == null) {

                    var options = MarkerOptions().position(it).snippet(result.get(0).getAddressLine(0))
                    marker = mMap!!.addMarker(options)
                } else {
                    marker!!.position = it
                    marker!!.snippet = result.get(0).getAddressLine(0)
                }

                marker!!.showInfoWindow()
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(it))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
