package com.fusecoords.drivedroid.Authority

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.fusecoords.drivedroid.Customer.AddLicence
import com.fusecoords.drivedroid.Customer.Licence
import com.fusecoords.drivedroid.Customer.LicenceAdapter
import com.fusecoords.drivedroid.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_fine_list_select.*

class FineListSelect : AppCompatActivity() {
    lateinit var items: ArrayList<Fine>
    var licenceTypeAdapter: FineSelectAdapter? = null

    companion object {
        var vehicleNo: String = ""
        var LicenceNo: String = ""
        var reportedId: String = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fine_list_select)

        items = ArrayList()
        licenceTypeAdapter = FineSelectAdapter(items, this)
        recylerType.adapter = licenceTypeAdapter
        recylerType.layoutManager = LinearLayoutManager(this)

        proceed.setOnClickListener {
            val filterdNames = ArrayList<Fine>()
            //looping through existing elements
            for (s in items) {
                //if the existing elements contains the search input
                if (s.isChecked) {
                    //adding the element to filtered list
                    filterdNames.add(s)
                }
            }
            for (item in items) {
                item.violationList = ArrayList()
            }
            ValidateFine.items = filterdNames
            ValidateFine.reportedId = reportedId
            ValidateFine.LicenceNo = LicenceNo
            ValidateFine.vehicleNo = vehicleNo
            startActivity(Intent(this, ValidateFine::class.java))
        }

        var mDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(Fine.DB_TABLE_LICENCE)

        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val filterdNames = ArrayList<Fine>()
                //looping through existing elements
                for (s in items) {
                    //if the existing elements contains the search input
                    if (s.Offence!!.toLowerCase().contains(search.text.toString().toLowerCase()) || s.Section!!.toLowerCase().contains(
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

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    items.clear()
                    for (snapshot in dataSnapshot.children) {
                        val bullet = snapshot.getValue<Fine>(Fine::class.java!!)
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
