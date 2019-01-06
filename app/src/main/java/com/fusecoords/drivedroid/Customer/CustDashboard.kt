package com.fusecoords.drivedroid.Customer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.fusecoords.drivedroid.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cust_dashboard.*

class CustDashboard : AppCompatActivity() {
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.my_toolbar))


        //fine paid and remaining -done only time calc remaining
        //toll pay sytem.
        //address from geolocation
        //change password ,email etc


        setContentView(R.layout.activity_cust_dashboard)

        add_licence!!.setOnClickListener {
            startActivity(Intent(this@CustDashboard, LicenceList::class.java))
        }

        add_vehicle!!.setOnClickListener {
            startActivity(Intent(this@CustDashboard, VehicleList::class.java))
        }
        view_fine.setOnClickListener {
            startActivity(Intent(this@CustDashboard, ViewFine::class.java))
        }
        view_status.setOnClickListener {
            startActivity(Intent(this@CustDashboard, ViewStatus::class.java))
        }

        check_towed.setOnClickListener {
            startActivity(Intent(this@CustDashboard, TowCheck::class.java))
        }
        view_alert.setOnClickListener {
            startActivity(Intent(this@CustDashboard, ViewAlert::class.java))
        }
        traffic_division.setOnClickListener {
            startActivity(Intent(this@CustDashboard, DivisonList::class.java))
        }
        traffic_rto.setOnClickListener {
            startActivity(Intent(this@CustDashboard, RtoList::class.java))
        }

        changePass.setOnClickListener {
            startActivity(Intent(this@CustDashboard, ChangePasswordActivity::class.java))
        }
        updateProfile.setOnClickListener {
            CustRegisterActivity.Flow=1
            startActivity(Intent(this@CustDashboard, CustRegisterActivity::class.java))
        }

        sign_out.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth!!.signOut()
            startActivity(Intent(this@CustDashboard, CustLoginActivity::class.java))
            finish()

        }
    }
}
