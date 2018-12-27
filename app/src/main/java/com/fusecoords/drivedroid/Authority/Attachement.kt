package com.fusecoords.drivedroid.Authority

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.fusecoords.drivedroid.Customer.CustUser
import com.fusecoords.drivedroid.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jsibbold.zoomage.ZoomageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_info.*

class Attachement : AppCompatActivity() {
    companion object {
        var flow: Int = 0
        var vehicleNo: String = ""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attachement)
        val image = findViewById(R.id.myZoomageView) as ZoomageView
        var mStorage: StorageReference =
            FirebaseStorage.getInstance().getReference()

        var path: String = ""
        if (flow == 1)
            path = CustUser.DB_IMAGE_RC
        else if (flow == 2)
            path = CustUser.DB_IMAGE_TAX
        else if (flow == 3)
            path = CustUser.DB_IMAGE_PUC
        else if (flow == 4)
            path = CustUser.DB_IMAGE_INS
        var full = path + "/" + vehicleNo + ".png";
        mStorage.child(path + "/" + vehicleNo + ".png").getDownloadUrl()
            .addOnSuccessListener(OnSuccessListener<Any> { uri ->
                System.out.println("Pass" + uri)
                Picasso.get().load(uri.toString()).into(image)
            }).addOnFailureListener(OnFailureListener {
                // Handle any errors
                System.out.println("Failuare")
            })

    }
}
