package com.fusecoords.drivedroid.Customer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fusecoords.drivedroid.Authority.Receipt
import com.fusecoords.drivedroid.Authority.Tow
import com.fusecoords.drivedroid.Authority.Violation
import com.fusecoords.drivedroid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_towed_check.*

class TowCheck : AppCompatActivity() {
    lateinit var items: ArrayList<Tow>
    var licenceTypeAdapter: TowAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_towed_check)
        items = ArrayList()
        licenceTypeAdapter = TowAdapter(items, this)
        recylerType.adapter = licenceTypeAdapter
        recylerType.layoutManager = LinearLayoutManager(this)

        search.setOnClickListener {
            var mDatabase: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(Tow.DB_TABLE_LICENCE)


            var query = mDatabase.orderByChild("VehicleNo").equalTo(searchText.text.toString())
            query.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        items.clear()
                        for (snapshot in dataSnapshot.children) {
                            var bullet = snapshot.getValue<Tow>(Tow::class.java!!)

                            items.add(bullet!!)
                        }
                        licenceTypeAdapter!!.notifyDataSetChanged()
                    }
                }

            })
        }


    }
}
