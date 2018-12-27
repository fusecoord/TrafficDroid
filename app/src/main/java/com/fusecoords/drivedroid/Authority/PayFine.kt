package com.fusecoords.drivedroid.Authority

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.BoringLayout
import android.widget.Toast
import com.fusecoords.drivedroid.Customer.CustUser
import com.fusecoords.drivedroid.Customer.Licence
import com.fusecoords.drivedroid.Customer.Vehicle
import com.fusecoords.drivedroid.R
import com.google.firebase.database.*
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_pay_fine.*
import java.util.ArrayList

class PayFine : AppCompatActivity() {
    var isLicence: Boolean = true;

    companion object {
        var flow: Int = 0;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                inputNo.setText(result.contents)
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
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

        proceed.setOnClickListener {
            var mDatabase: DatabaseReference
            var key: String = ""
            if (!isLicence) {
                key = "VehicleNo"
                mDatabase = FirebaseDatabase.getInstance().getReference(Vehicle.DB_TABLE_VEHICLE)
            } else {
                key = "LicenceNo"
                mDatabase = FirebaseDatabase.getInstance().getReference(Licence.DB_TABLE_LICENCE)
            }
            val query = mDatabase.orderByChild(key).equalTo(inputNo.text.toString())
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (flow == 1) {
                            for (snapshot in dataSnapshot.children) {
                                if (!isLicence) {
                                    var bullet = snapshot.getValue<Vehicle>(Vehicle::class.java!!)
                                    FineListSelect.reportedId = bullet!!.UserId!!
                                    FineListSelect.vehicleNo = inputNo.text.toString()
                                    FineListSelect.LicenceNo = ""
                                } else {
                                    var bullet = snapshot.getValue<Licence>(Licence::class.java!!)
                                    FineListSelect.reportedId = bullet!!.UserId!!
                                    FineListSelect.vehicleNo = ""
                                    FineListSelect.LicenceNo = inputNo.text.toString()
                                }
                            }
                            startActivity(Intent(this@PayFine, FineListSelect::class.java))
                        } else {
                            for (snapshot in dataSnapshot.children) {
                                if (!isLicence) {
                                    var bullet = snapshot.getValue<Vehicle>(Vehicle::class.java!!)
                                    UserInfoActivity.reportedId = bullet!!.UserId!!
                                    UserInfoActivity.vehicleNo = inputNo.text.toString()
                                    UserInfoActivity.LicenceNo = ""
                                } else {
                                    var bullet = snapshot.getValue<Licence>(Licence::class.java!!)
                                    UserInfoActivity.reportedId = bullet!!.UserId!!
                                    UserInfoActivity.vehicleNo = ""
                                    UserInfoActivity.LicenceNo = inputNo.text.toString()
                                }
                            }
                            startActivity(Intent(this@PayFine, UserInfoActivity::class.java))
                        }
                    } else {
                        // Do stuff
                    }
                }


            })

        }
        showVehicle.setOnClickListener {
            isLicence = false;
            inputNo.clearFocus()
            inputNo.setHint("Enter Vehicle No or Scan it")
            inputNo.setText("")
        }
    }
}
