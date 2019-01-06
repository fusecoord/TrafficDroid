package com.fusecoords.drivedroid.Customer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fusecoords.drivedroid.Authority.MainActivity
import com.fusecoords.drivedroid.Authority.User
import com.fusecoords.drivedroid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.activity_licence_list.*


class LicenceList : AppCompatActivity() {
    lateinit var items: ArrayList<Licence>
    var licenceTypeAdapter: LicenceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licence_list)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        items = ArrayList()
        licenceTypeAdapter = LicenceAdapter(items, this)
        recylerType.adapter = licenceTypeAdapter
        recylerType.layoutManager = LinearLayoutManager(this)
        var licenceType = Licence()

        addType.setOnClickListener { startActivity(Intent(this, AddLicence::class.java)) }
        var mDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(Licence.DB_TABLE_LICENCE)
        val query = mDatabase.orderByChild("UserId").equalTo(FirebaseAuth.getInstance().currentUser!!.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    items.clear()
                    for (snapshot in dataSnapshot.children) {
                        val bullet = snapshot.getValue<Licence>(Licence::class.java!!)

                        var mDatabase: DatabaseReference =
                            FirebaseDatabase.getInstance().getReference(LicenceType.DB_TABLE_LICENCE_TYPE)
                        val query = mDatabase.orderByChild("LicenceId").equalTo(snapshot.key)
                        query.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    var types = ArrayList<LicenceType>()
                                    for (snapshot in dataSnapshot.children) {
                                        val bullet1 = snapshot.getValue<LicenceType>(LicenceType::class.java!!)
                                        types.add(bullet1!!)
                                    }
                                    bullet!!.licenceType = types
                                    licenceTypeAdapter!!.notifyDataSetChanged()
                                } else {
                                    // Do stuff
                                }
                            }


                        })
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
