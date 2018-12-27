package com.fusecoords.drivedroid.Customer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.fusecoords.drivedroid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_divison_list.*

class DivisonList : AppCompatActivity() {
    lateinit var items: ArrayList<Division>
    var licenceTypeAdapter: DivisionAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_divison_list)
        items = ArrayList()
        licenceTypeAdapter = DivisionAdapter(items, this)
        recylerType.adapter = licenceTypeAdapter
        recylerType.layoutManager = LinearLayoutManager(this)

        var mDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(Division.DB_TABLE_LICENCE)
        // val query = mDatabase.orderByChild("UserId").equalTo(FirebaseAuth.getInstance().currentUser!!.uid)
        mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    items.clear()
                    for (snapshot in dataSnapshot.children) {
                        val bullet = snapshot.getValue<Division>(Division::class.java!!)
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
                val filterdNames = ArrayList<Division>()
                //looping through existing elements
                for (s in items) {
                    //if the existing elements contains the search input
                    if (s.Name!!.toLowerCase().contains(search.text.toString().toLowerCase()) || s.Address!!.toLowerCase().contains(
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
