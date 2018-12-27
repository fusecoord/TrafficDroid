package com.fusecoords.drivedroid

import android.Manifest
import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.analytics.FirebaseAnalytics
import java.text.SimpleDateFormat
import java.util.*

class CustomApp : Application() {
    var mFirebaseAnalytics: FirebaseAnalytics? = null;
    var isAppTerminated = true
    var locationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (5 * 1000).toLong()  /* 5 secs */
    private val FASTEST_INTERVAL: Long = UPDATE_INTERVAL / 2 /* 2.5 sec */
    private lateinit var locationCallback: LocationCallback

    var currentLocation: LatLng? = LatLng(0.0, 0.0)


    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onCreate() {
        super.onCreate()
        isAppTerminated = false
        locationProviderClient = getFusedLocationProviderClient(this)
        mLocationRequest = LocationRequest()
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest!!.setInterval(UPDATE_INTERVAL)
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    var intent = Intent(GPS_UPDATE_TAG);
                    intent.putExtra("latitude", location.latitude);
                    intent.putExtra("longitude", location.longitude);
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
                    break
                }
            }
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        if (BuildConfig.EnableAnalytics) {
//
//            Fabric.with(this, Crashlytics())
//            logUser()
//        }

    }


    @TargetApi(Build.VERSION_CODES.M)
    fun getCurrentLocation(afragment: AppCompatActivity) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val listPermissionsNeeded = ArrayList<String>()
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (!listPermissionsNeeded.isEmpty())
                afragment.requestPermissions(
                    listPermissionsNeeded.toTypedArray(),
                    SplashActivity.REQUEST_ID_MULTIPLE_PERMISSIONS
                )
        } else {
            locationProviderClient!!.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        currentLocation = LatLng(location.latitude, location.longitude)
                        var intent = Intent(GPS_CURRENT_TAG);
                        intent.putExtra("latitude", location.latitude);
                        intent.putExtra("longitude", location.longitude);
                        intent.putExtra("provided", location.provider)

                    } else {
                        var intent = Intent(GPS_CURRENT_TAG);
                        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
                    }
                }
                .addOnFailureListener {
                    //Log.d("Exception", it.toString())
                }
        }
    }

    fun getDate(date: Date): String {
        val format = SimpleDateFormat("dd/MM/yyy")
        return format.format(date)
    }

    fun getDateTime(date: Date): String {
        val format = SimpleDateFormat("dd/MM/yyy hh:mm:ss a")
        return format.format(date)
    }
    fun startLocationUpdates(activity: AppCompatActivity) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            val listPermissionsNeeded = ArrayList<String>()
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            if (!listPermissionsNeeded.isEmpty())
                requestPermissions(
                    activity,
                    listPermissionsNeeded.toTypedArray(),
                    SplashActivity.REQUEST_ID_MULTIPLE_PERMISSIONS
                )
        } else {
            locationProviderClient!!.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper())

        }
    }

//    fun startLocationUpdates(fragment: AppCompatActivity) {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            val listPermissionsNeeded = ArrayList<String>()
//            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
//            if (!listPermissionsNeeded.isEmpty())
//                fragment.requestPermissions(
//                    listPermissionsNeeded.toTypedArray(),
//                    SplashActivity.REQUEST_ID_MULTIPLE_PERMISSIONS
//                )
//        } else {
//            stopLocationUpdates()
//            locationProviderClient!!.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper())
//        }
//    }

    fun stopLocationUpdates() {
        locationProviderClient!!.removeLocationUpdates(locationCallback)
    }

    private fun logUser() {

        // You can call any combination of these three methods
//        val userinfo: UserInfo = SharedPreferenceHelper.getInstance(this).getUserInfo(SharedPreferenceHelper.userKey)!!
//        if (userinfo != null) {
//            Crashlytics.setUserIdentifier(userinfo!!.userId.toString());
//            Crashlytics.setUserEmail(userinfo!!.email);
//        }
    }


    fun getImei(): String {

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val telephonyManager =
                    applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                var imei = ""
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = telephonyManager.getImei();
                } else {
                    imei = telephonyManager.getDeviceId();
                }
                Log.e("imei", "=" + imei!!)
                return if (imei != null && !imei.isEmpty()) {
                    imei
                } else {
                    android.os.Build.SERIAL
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return ""
    }


    companion object {
        var APP_TYPE = "2"
        val PLAY_STORE_URL = "http://play.google.com/store/apps/details?id=com.plexitech.bestwestern"
        var GPS_UPDATE_TAG = "user-location-update"
        var GPS_CURRENT_TAG = "user-location-current"
    }
}