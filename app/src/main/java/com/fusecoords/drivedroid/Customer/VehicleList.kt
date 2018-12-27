package com.fusecoords.drivedroid.Customer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fusecoords.drivedroid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_vehicle_list.*

class VehicleList : AppCompatActivity() {

    lateinit var items: ArrayList<Vehicle>
    var licenceTypeAdapter: VehicleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)
        items = ArrayList()
        licenceTypeAdapter = VehicleAdapter(items, this)
        recylerType.adapter = licenceTypeAdapter
        recylerType.layoutManager = LinearLayoutManager(this)

        addVehicle.setOnClickListener { startActivity(Intent(this, AddVehicle::class.java)) }
        var mDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(Vehicle.DB_TABLE_VEHICLE)
        val query = mDatabase.orderByChild("UserId").equalTo(FirebaseAuth.getInstance().currentUser!!.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    items.clear()
                    for (snapshot in dataSnapshot.children) {
                        val bullet = snapshot.getValue<Vehicle>(Vehicle::class.java!!)
                        items.add(bullet!!)
                    }
                    licenceTypeAdapter!!.notifyDataSetChanged()
                } else {
                    // Do stuff
                }
            }


        })
    }
}
