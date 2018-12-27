package com.fusecoords.drivedroid

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.fusecoords.drivedroid.Authority.LoginActivity
import com.fusecoords.drivedroid.BuildConfig.USER_TYPE
import com.fusecoords.drivedroid.Customer.CustLoginActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    var splash_duration: Long = 2500;
    private val TAG = "FragmentActivity"

    fun getServicesAvailable(): Boolean {
        val api = GoogleApiAvailability.getInstance()
        val isAvailable = api.isGooglePlayServicesAvailable(this)
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true
        } else if (api.isUserResolvableError(isAvailable)) {
            val dialog = api.getErrorDialog(this, isAvailable, 0)
            dialog.show()
        } else {
            Toast.makeText(this, "Cannot Connect To Play Services", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        return isConnected
    }

    fun checkLocationSetting(): Boolean {
//        var service = mActivity!!.getSystemService(Application.LOCATION_SERVICE) as LocationManager
//        var enabled = service!!.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        return enabled

        var gps_enabled = false;
        var network_enabled = false;
        var locationManager: LocationManager? = null;
        try {
            if (locationManager == null && !isFinishing)
                locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager;

            gps_enabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (ex: Exception) {
            //do nothing...
        }
        return !gps_enabled && !network_enabled
    }

    override fun onStart() {
        super.onStart()
        if (isPlayServiceAvailable()) {
            if (isNetworkAvailable()) {
                if (!checkLocationSetting()) {
                    if (checkAndRequestPermissions()) {
                        Handler().postDelayed({
                            processFlow()
                        }, splash_duration)
                    }
                } else {
                    button_dismiss.visibility = View.GONE
                    button_permission.visibility = View.GONE
                    parent_permission.visibility = View.VISIBLE
                    txt_permission.text = "GPS location is required, please enable location and restart the app."
                    button_permission.setOnClickListener { checkAndRequestPermissions() }
                    button_dismiss.setOnClickListener { this.finish() }
                }
            } else {
                button_dismiss.visibility = View.GONE
                button_permission.visibility = View.GONE
                parent_permission.visibility = View.VISIBLE
                txt_permission.text =
                        "Internet connection is required, please enable internet connection and restart the app."
                button_permission.setOnClickListener { checkAndRequestPermissions() }
                button_dismiss.setOnClickListener { this.finish() }
            }
        } else {
            button_dismiss.visibility = View.GONE
            button_permission.visibility = View.GONE
            parent_permission.visibility = View.VISIBLE
            txt_permission.text = "Google Play Service is required in order to use this app."
            button_permission.setOnClickListener { checkAndRequestPermissions() }
            button_dismiss.setOnClickListener { this.finish() }
        }
    }

    fun isPlayServiceAvailable(): Boolean {
        val api = GoogleApiAvailability.getInstance()
        val isAvailable = api.isGooglePlayServicesAvailable(this)
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true
        } else
            return false
    }

    fun processFlow() {

        Log.d("CustType", "UserType" + USER_TYPE)
        if (USER_TYPE == 1) {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        } else if (USER_TYPE == 2) {
            startActivity(Intent(this@SplashActivity, CustLoginActivity::class.java))
        } else if (USER_TYPE == 3) {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }
        (application as CustomApp).startLocationUpdates(this)
        finish()


    }



    @SuppressLint("NewApi")
    private fun checkAndRequestPermissions(): Boolean {
        val listPermissionsNeeded = ArrayList<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE)
        }
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
//        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE)
//        }
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }



    @SuppressLint("NewApi")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                // Initialize the map with both permissions
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
              //  perms[Manifest.permission.CALL_PHONE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_PHONE_STATE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.SEND_SMS] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.size > 0) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]

                    if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.SEND_SMS] == PackageManager.PERMISSION_GRANTED
                       // && perms[Manifest.permission.CALL_PHONE] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.READ_PHONE_STATE] == PackageManager.PERMISSION_GRANTED
                    ) {
                        parent_permission.visibility = View.GONE
                        processFlow()
                    } else {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                           || shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)
                            || shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS)
                            //|| shouldShowRequestPermissionRationale(android.Manifest.permission.CALL_PHONE)
                            || shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            || shouldShowRequestPermissionRationale(android.Manifest.permission.READ_PHONE_STATE)
                        ) {
                            parent_permission.visibility = View.VISIBLE
                            txt_permission.text =
                                    "All permission are required in order to provide functionality, Would you like to review permission again?"
                            button_permission.setOnClickListener { checkAndRequestPermissions() }
                            button_dismiss.setOnClickListener { this.finish() }
                        } else {
                            parent_permission.visibility = View.VISIBLE
                            txt_permission.text =
                                    "You need to give required permissions to continue.Do you want to go to app settings?"
                            button_permission.setOnClickListener {
                                startActivity(
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:" + this.packageName)
                                    )
                                )
                            }
                            button_dismiss.setOnClickListener { this.finish() }
                        }
                    }
                }
            }
        }

    }

    companion object {
        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
        private val SPLASH_TIME_OUT = 2000
    }
}
