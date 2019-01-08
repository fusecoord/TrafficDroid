package com.fusecoords.drivedroid.Authority

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.fusecoords.drivedroid.CustomApp
import com.fusecoords.drivedroid.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_history.*
import java.util.*

class History : AppCompatActivity() {


    companion object {
        var items: ArrayList<Violation>? = null
        var reportedId: String = ""
        var vehicleNo: String = ""
        var LicenceNo: String = ""
    }

    var licenceTypeAdapter: HistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        items = ArrayList()

        var mDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(Receipt.DB_TABLE_LICENCE)

        val query = mDatabase.child(reportedId)
        query.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(receiptdatasnapshot: DataSnapshot) {
                    if (receiptdatasnapshot.exists()) {
                        var mDatabase: DatabaseReference =
                            FirebaseDatabase.getInstance().getReference(Fine.DB_TABLE_LICENCE)
                        mDatabase.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                            }

                            override fun onDataChange(dataSnapshot1: DataSnapshot) {
                                if (dataSnapshot1.exists()) {

                                    for (snapshot in receiptdatasnapshot.children) {
                                        val bullet = snapshot.getValue<Violation>(Violation::class.java)


                                        for (snapshot in dataSnapshot1.children) {
                                            val fine = snapshot.getValue<Fine>(Fine::class.java)

                                            if (fine!!.Sr_No == bullet!!.FineId) {
                                                bullet.TotalAmount = fine.Penalty
                                                items!!.add(bullet!!)
                                            }
                                        }

                                        licenceTypeAdapter = HistoryAdapter(items!!, this@History)
                                        recylerType.layoutManager = LinearLayoutManager(this@History)
                                        recylerType.adapter = licenceTypeAdapter
                                        /*for (item in items!!) {
                                            bullet!!.Id = snapshot.ref.key!!
                                            if (item.Sr_No == bullet!!.FineId) {
                                                var previous = item.violationList
                                                previous.add(bullet)
                                                item.violationList = previous
                                            }
                                        }*/
                                    }


                                } else {
                                    // Do stuff
                                }
                            }
                        })


                    } else {
                        // Do stuff
                    }

                }


            })

    }
}
