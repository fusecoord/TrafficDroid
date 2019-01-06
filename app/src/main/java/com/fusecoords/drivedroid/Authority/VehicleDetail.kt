package com.fusecoords.drivedroid.Authority

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fusecoords.drivedroid.Customer.Licence
import com.fusecoords.drivedroid.Customer.LicenceType
import com.fusecoords.drivedroid.Customer.LicenceTypeAdapter
import com.fusecoords.drivedroid.Customer.Vehicle
import com.fusecoords.drivedroid.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_vehicle_detail.*

class VehicleDetail : AppCompatActivity() {
    companion object {
        var vehicleNoStr: String = ""

    }

    lateinit var items: ArrayList<LicenceType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_detail)
        LicenceTypeAdapter.flow = 1
        items = ArrayList()


        var mDatabase = FirebaseDatabase.getInstance().getReference(Vehicle.DB_TABLE_VEHICLE)
        var query = mDatabase.orderByChild("VehicleNo").equalTo(vehicleNoStr)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        var bullet = snapshot.getValue<Vehicle>(Vehicle::class.java!!)
                        vehicleNo.setText(bullet!!.VehicleNo)
                        regNo.setText(bullet!!.RegNo)
                        regDate.setText(bullet!!.RegDate)
                        model.setText(bullet!!.Model)
                        classv.setText(bullet!!.Class)
                        chasis.setText(bullet!!.ChasisNo)
                        engineNo.setText(bullet!!.EngineNo)
                        fuelType.setText(bullet!!.FuelType)
                        rcexpiry.setText(bullet!!.RcExpiry)


                    }
                }
            }

        })


    }
}
