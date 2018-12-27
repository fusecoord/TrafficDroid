package com.fusecoords.drivedroid.Customer

import android.graphics.Bitmap
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.widget.Toast
import com.fusecoords.drivedroid.Authority.Alert
import com.fusecoords.drivedroid.Authority.PhotoFragment
import com.fusecoords.drivedroid.Authority.ValidateFine
import com.fusecoords.drivedroid.CustomApp
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
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_send_alert.*
import kotlinx.android.synthetic.main.activity_view_alert.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ViewAlert : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    var items: ArrayList<Alert>? = null
    var marker: Marker? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        items = ArrayList()
        setContentView(R.layout.activity_view_alert)
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

        } catch (e: Exception) {
            e.printStackTrace()

        }

        var mDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(Alert.DB_TABLE_LICENCE)
        //
        var currentDate = (application as CustomApp).getDate(Date())
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    items!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val bullet = snapshot.getValue<Alert>(Alert::class.java!!)
                        if (bullet!!.Date.equals(currentDate))
                            items!!.add(bullet!!)
                    }
                    if (items!!.size > 0) {
                        pager.clipToPadding = false;
                        pager.offscreenPageLimit = items!!.size
                        pager.setPadding(20, 0, 20, 0);
                        var pagerAdapter: MoviesPagerAdapter
                        pagerAdapter = MoviesPagerAdapter(supportFragmentManager, items!!)
                        pager.adapter = pagerAdapter
                        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                            override fun onPageScrollStateChanged(p0: Int) {

                            }

                            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                            }

                            override fun onPageSelected(p0: Int) {
                                addMarkers(p0)
                            }

                        })

                        addMarkers(0)
                    }
                } else {
                    // Do stuff
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })


    }

    fun addMarkers(postions: Int) {
        try {
            var aler = items!!.get(postions)
            if (marker == null) {
                marker = mMap!!.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            aler.Latitude,
                            aler.Longitude
                        )
                    ).snippet(AddressLocation.Address)
                )
            } else
                marker!!.position = LatLng(
                    aler.Latitude,
                    aler.Longitude
                )
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(marker!!.position, 16.0f))
        } catch (e: Exception) {
        }

    }


    class MoviesPagerAdapter(fragmentManager: FragmentManager, private val movies: ArrayList<Alert>) :
        FragmentStatePagerAdapter(fragmentManager) {

        // 2
        override fun getItem(position: Int): Fragment {
            AlertFragment.Alert = movies.get(position)
            return AlertFragment()
        }

        // 3
        override fun getCount(): Int {
            return movies.size
        }

    }
}
