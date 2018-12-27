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
    private var btnAddLicence: Button? = null
    private var btnAddVehicle: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //upload docs -done
        //offence list view-done
        //fine paid and remaining -done only time calc remaining
        //toll pay sytem.
        //address from geolocation
        //change password ,email etc


        //tow alert systme
//       traffice alerts -done
//        permisison check with locaiton -done

        setContentView(R.layout.activity_cust_dashboard)
        btnAddLicence = findViewById(R.id.add_licence) as Button
        btnAddLicence!!.setOnClickListener {
            startActivity(Intent(this@CustDashboard, LicenceList::class.java))
        }
        btnAddVehicle = findViewById(R.id.add_vehicle) as Button
        btnAddVehicle!!.setOnClickListener {
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

        sign_out.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth!!.signOut()
            finish()
        }
    }
}
