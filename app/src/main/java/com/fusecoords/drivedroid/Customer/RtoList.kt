package com.fusecoords.drivedroid.Customer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.fusecoords.drivedroid.Authority.Fine
import com.fusecoords.drivedroid.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_rto_list.*

class RtoList : AppCompatActivity() {
    lateinit var items: ArrayList<RtoOffice>
    var licenceTypeAdapter: RtoAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rto_list)
        items = ArrayList()
        licenceTypeAdapter = RtoAdapter(items, this)
        recylerType.adapter = licenceTypeAdapter
        recylerType.layoutManager = LinearLayoutManager(this)

        var mDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(RtoOffice.DB_TABLE_LICENCE)
        // val query = mDatabase.orderByChild("UserId").equalTo(FirebaseAuth.getInstance().currentUser!!.uid)
        mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    items.clear()
                    for (snapshot in dataSnapshot.children) {
                        val bullet = snapshot.getValue<RtoOffice>(RtoOffice::class.java!!)
                        items.add(bullet!!)
                    }
                    licenceTypeAdapter!!.notifyDataSetChanged()
                } else {
                    // Do stuff
                }
            }


        })

        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filterdNames = ArrayList<RtoOffice>()
                //looping through existing elements
                for (s in items) {
                    //if the existing elements contains the search input
                    if (s.Offices!!.toLowerCase().contains(search.text.toString().toLowerCase()) || s.Code!!.toLowerCase().contains(
                            search.text.toString().toLowerCase()
                        )
                    ) {
                        //adding the element to filtered list
                        filterdNames.add(s)
                    }
                }
                //calling a method of the adapter class and passing the filtered list
                licenceTypeAdapter!!.filterList(filterdNames)
            }
        })
    }
}
