package com.fusecoords.drivedroid.Authority

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.BoringLayout
import android.view.View
import android.widget.Toast
import com.fusecoords.drivedroid.Customer.CustUser
import com.fusecoords.drivedroid.Customer.Licence
import com.fusecoords.drivedroid.Customer.Vehicle
import com.fusecoords.drivedroid.R
import com.google.firebase.database.*
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_pay_fine.*
import java.util.ArrayList
import android.widget.TabHost


class PayFine : AppCompatActivity() {


    companion object {
        var flow: Int = 0;
    }

    var mTabHost: TabHost? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show()
            } else {
                if (mTabHost!!.currentTab == 1) {
                    inputVehicleNo.setText(result.contents)
                } else
                    inputNo.setText(result.contents)
                // Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_fine)
        scan.setOnClickListener {
            IntentIntegrator(this).initiateScan();
        }
        mTabHost = findViewById<View>(R.id.tabHost) as TabHost
        mTabHost!!.setup()
        //Lets add the first Tab
        var mSpec: TabHost.TabSpec = mTabHost!!.newTabSpec("Licence No")
        mSpec.setContent(R.id.first_Tab)
        mSpec.setIndicator("Licence No")
        mTabHost!!.addTab(mSpec)
        //Lets add the second Tab
        mSpec = mTabHost!!.newTabSpec("Vehicle No")
        mSpec.setContent(R.id.second_Tab)
        mSpec.setIndicator("Vehicle No")
        mTabHost!!.addTab(mSpec)
        //Lets add the third Tab

        proceed.setOnClickListener {
            var mDatabase: DatabaseReference
            var key: String = ""
            var input: String = ""
            if (mTabHost!!.currentTab == 1) {
                key = "VehicleNo"
                mDatabase = FirebaseDatabase.getInstance().getReference(Vehicle.DB_TABLE_VEHICLE)
                input = inputVehicleNo.text.toString()
            } else {
                key = "LicenceNo"
                mDatabase = FirebaseDatabase.getInstance().getReference(Licence.DB_TABLE_LICENCE)
                input = inputNo.text.toString()
            }
            val query = mDatabase.orderByChild(key).equalTo(input)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (flow == 1) {
                            for (snapshot in dataSnapshot.children) {
                                if (mTabHost!!.currentTab == 1) {
                                    var bullet = snapshot.getValue<Vehicle>(Vehicle::class.java!!)
                                    FineListSelect.reportedId = bullet!!.UserId!!
                                    FineListSelect.vehicleNo = input
                                    FineListSelect.LicenceNo = ""
                                } else {
                                    var bullet = snapshot.getValue<Licence>(Licence::class.java!!)
                                    FineListSelect.reportedId = bullet!!.UserId!!
                                    FineListSelect.vehicleNo = ""
                                    FineListSelect.LicenceNo = input
                                }
                            }
                            startActivity(Intent(this@PayFine, FineListSelect::class.java))
                        } else  if (flow == 2){
                            for (snapshot in dataSnapshot.children) {
                                if (mTabHost!!.currentTab == 1) {
                                    var bullet = snapshot.getValue<Vehicle>(Vehicle::class.java!!)
                                    UserInfoActivity.reportedId = bullet!!.UserId!!
                                    UserInfoActivity.vehicleNo = input
                                    UserInfoActivity.LicenceNo = ""
                                } else {
                                    var bullet = snapshot.getValue<Licence>(Licence::class.java!!)
                                    UserInfoActivity.reportedId = bullet!!.UserId!!
                                    UserInfoActivity.vehicleNo = ""
                                    UserInfoActivity.LicenceNo = input
                                }
                            }
                            startActivity(Intent(this@PayFine, UserInfoActivity::class.java))
                        }else  if (flow == 3){
                            for (snapshot in dataSnapshot.children) {
                                if (mTabHost!!.currentTab == 1) {
                                    var bullet = snapshot.getValue<Vehicle>(Vehicle::class.java!!)
                                    History.reportedId = bullet!!.UserId!!
                                    History.vehicleNo = input
                                    History.LicenceNo = ""
                                } else {
                                    var bullet = snapshot.getValue<Licence>(Licence::class.java!!)
                                    History.reportedId = bullet!!.UserId!!
                                    History.vehicleNo = ""
                                    History.LicenceNo = input
                                }
                            }
                            startActivity(Intent(this@PayFine, History::class.java))
                        }
                    } else {
                        // Do stuff
                    }
                }


            })

        }

    }
}
