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
import kotlinx.android.synthetic.main.activity_add_licence.*
import com.google.firebase.database.DataSnapshot


class LicenceList : AppCompatActivity() {
    lateinit var items: ArrayList<Licence>
    var licenceTypeAdapter: LicenceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_licence_list)
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
