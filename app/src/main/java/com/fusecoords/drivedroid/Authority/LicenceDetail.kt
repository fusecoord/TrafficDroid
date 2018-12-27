package com.fusecoords.drivedroid.Authority

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.fusecoords.drivedroid.Customer.*
import com.fusecoords.drivedroid.R
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_licence_detail.*
import java.io.ByteArrayOutputStream

class LicenceDetail : AppCompatActivity() {
    companion object {
        var vehicleNo: String = ""
        var LicenceNo: String = ""
        var reportedId: String = ""
    }

    lateinit var items: ArrayList<LicenceType>
    var licenceTypeAdapter: LicenceTypeAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licence_detail)

        items = ArrayList()
        licenceTypeAdapter = LicenceTypeAdapter(items, this)
        recylerType.adapter = licenceTypeAdapter
        recylerType.layoutManager = LinearLayoutManager(this)


        var mDatabase = FirebaseDatabase.getInstance().getReference(Licence.DB_TABLE_LICENCE)
        var query = mDatabase.orderByChild("LicenceNo").equalTo(LicenceNo)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        var bullet = snapshot.getValue<Licence>(Licence::class.java!!)
                        licenceNo.setText(bullet!!.LicenceNo)
                        doi.setText(bullet!!.Doi)
                        drivingexpiry.setText(bullet!!.Doe)
                        dld.setText(bullet!!.Dld)
                        issueauth.setText(bullet!!.Auth)

                        var mDatabase: DatabaseReference =
                            FirebaseDatabase.getInstance().getReference(LicenceType.DB_TABLE_LICENCE_TYPE)

                        var query = mDatabase.orderByChild("LicenceId").equalTo(snapshot.ref.key)
                        query.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (snapshot in dataSnapshot.children) {
                                        var bullet = snapshot.getValue<LicenceType>(LicenceType::class.java!!)
                                        items.add(bullet!!)

                                    }
                                    licenceTypeAdapter!!.notifyDataSetChanged()
                                }
                            }

                        })
                    }
                }
            }

        })


    }
}
