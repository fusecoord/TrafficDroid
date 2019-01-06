package com.fusecoords.drivedroid.Authority

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.fusecoords.drivedroid.Customer.CustDashboard
import com.fusecoords.drivedroid.Customer.CustUser
import com.fusecoords.drivedroid.Customer.LicenceType
import com.fusecoords.drivedroid.Customer.Vehicle
import com.fusecoords.drivedroid.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_info.*

class UserInfoActivity : AppCompatActivity() {

    companion object {
        var vehicleNo: String = ""
        var LicenceNo: String = ""
        var reportedId: String = ""
    }

    lateinit var items: ArrayList<Vehicle>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        if (vehicleNo.isEmpty()) {
            reg.isEnabled = false
            taxation.isEnabled = false
            puc.isEnabled = false
            insurance.isEnabled = false
        }
        items = ArrayList()
        val user = FirebaseAuth.getInstance().currentUser



        val image = findViewById(R.id.img_profile) as ImageView
        licence.setOnClickListener {
            LicenceDetail.LicenceNo = LicenceNo
            startActivity(Intent(this@UserInfoActivity, LicenceDetail::class.java))
        }
        vehicleInfo.setOnClickListener {
            VehicleDetail.vehicleNoStr = vehicleNo
            startActivity(Intent(this@UserInfoActivity, VehicleDetail::class.java))
        }
        puc.setOnClickListener {
            goTo(3, vehicleNo)
        }
        reg.setOnClickListener {
            goTo(1, vehicleNo)
        }
        insurance.setOnClickListener {
            goTo(4, vehicleNo)
        }
        taxation.setOnClickListener {
            goTo(2, vehicleNo)
        }
        if (vehicleNo.equals("")) {
            taxation.visibility = View.GONE
            puc.visibility = View.GONE
            reg.visibility = View.GONE
            insurance.visibility = View.GONE
            vehicleInfo.visibility=View.GONE
        }
        if (LicenceNo.equals(""))
            licence.visibility = View.GONE


        var mStorage: StorageReference =
            FirebaseStorage.getInstance()
                .getReference(CustUser.DB_IMAGE_PATH + "/" +reportedId + ".png")
        var url = mStorage.getDownloadUrl()
        url.addOnSuccessListener(OnSuccessListener<Any> { uri ->
            System.out.println("Pass" + uri)
            Picasso.get().load(uri.toString()).into(image)
        }).addOnFailureListener(OnFailureListener {
            // Handle any errors
            System.out.println("Failuare")
        })


        var mDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(CustUser.DB_USER_PATH)
        mDatabase.child(reportedId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //  for (snapshot in dataSnapshot.children) {

                        var bullet = dataSnapshot.getValue<CustUser>(CustUser::class.java!!)

                        email.setText(bullet!!.Email)
                        firstname.setText(bullet!!.FirstName)
                        lastname.setText(bullet!!.LastName)
                        fathername.setText(bullet!!.Parent)
                        dob.setText(bullet!!.Dob)
                        bloodgroup.setText(bullet!!.BloodGroup)
                        email.setText(bullet!!.Email)
                        phone.setText(bullet!!.Contact)
                        address.setText(bullet!!.Address)
                        pincode.setText(bullet!!.PinCode)

                        // }
                    } else {
                        // Do stuff
                    }
                }

            })


        mDatabase = FirebaseDatabase.getInstance().getReference(Vehicle.DB_TABLE_VEHICLE)

        var query = mDatabase.orderByChild("UserId").equalTo(reportedId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        var bullet = snapshot.getValue<Vehicle>(Vehicle::class.java!!)
                        items.add(bullet!!)

                    }
                }
            }

        })


    }

    fun goTo(flow: Int, vehicleNo: String) {

        Attachement.flow = flow
        Attachement.vehicleNo = vehicleNo
        startActivity(Intent(this@UserInfoActivity, Attachement::class.java))
    }

}
