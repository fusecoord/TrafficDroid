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
import kotlinx.android.synthetic.main.activity_validate_fine.*
import java.util.*

class ValidateFine : AppCompatActivity() {
    companion object {
        var items: ArrayList<Fine>? = null
        var reportedId: String = ""
        var vehicleNo: String = ""
        var LicenceNo: String = ""
    }

    var licenceTypeAdapter: FineValidateAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate_fine)

        proceed.setOnClickListener {
            for (item in items!!) {
                var user = Receipt();

                user.Date = (application as CustomApp).getDateTime(Date())
                user.FineId = item.Sr_No
                user.Latitude = (application as CustomApp).currentLocation!!.latitude
                user.Longitude = (application as CustomApp).currentLocation!!.longitude
                user.ReportedId = reportedId
                user.VehicleNo = vehicleNo
                user.LicenceNo = LicenceNo
                user.Offence = item.Offence
                user.Section = item.Section
                user.LicenceNo = LicenceNo
                var previousId = ""

                for (prev in item.violationList) {
                    if (previousId.equals(""))
                        previousId = prev.Id
                    else
                        previousId = previousId + "|" + prev.Id
                }
                user.PreviousViolationID = previousId
                user.UserId = FirebaseAuth.getInstance().currentUser!!.uid

                var amout = item.Penalty
                if (item.violationList.count() > 0) {
                    amout = amout * item.violationList.count()
                }
                user.TotalAmount = amout

                var mDatabase: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference(Receipt.DB_TABLE_LICENCE)
                var refer = mDatabase.child(reportedId).push()
                mDatabase.child(reportedId).child(refer.key!!)
                    .setValue(user.getHashMap())
                    .addOnCompleteListener(OnCompleteListener {
                        if (it.isSuccessful) {

                            Toast.makeText(
                                this@ValidateFine, "Register update complete.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@ValidateFine, "Register update failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }


        }

        var mDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(Receipt.DB_TABLE_LICENCE)

        val query = mDatabase.child(reportedId)
        query.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {


                        for (snapshot in dataSnapshot.children) {
                            val bullet = snapshot.getValue<Violation>(Violation::class.java)

                            for (item in items!!) {
                                bullet!!.Id = snapshot.ref.key!!
                                if (item.Sr_No == bullet!!.FineId) {
                                    var previous = item.violationList
                                    previous.add(bullet)
                                    item.violationList = previous
                                }
                            }
                        }

                    } else {
                        // Do stuff
                    }
                    var totalAmount = 0
                    for (item in items!!) {
                        totalAmount += item.Penalty
                        for (violation in item.violationList) {
                            totalAmount += item.Penalty
                        }
                    }
                    proceed.setText("Proceed (Total: ₹" + totalAmount + ")")

                    licenceTypeAdapter = FineValidateAdapter(items!!, this@ValidateFine)
                    recylerType.layoutManager = LinearLayoutManager(this@ValidateFine)
                    recylerType.adapter = licenceTypeAdapter
                }


            })

    }
}
