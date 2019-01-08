package com.fusecoords.drivedroid.Authority

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.fusecoords.drivedroid.CustomApp
import com.fusecoords.drivedroid.Customer.CustUser
import com.fusecoords.drivedroid.Customer.Licence
import com.fusecoords.drivedroid.Customer.Vehicle
import com.fusecoords.drivedroid.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_tow_vehicle.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class TowVehicle : AppCompatActivity() {
    private val PICK_IMAGE = 1000
    private val PICK_CAMERA = 1001
    var pagerAdapter: PhotoAdapter? = null
    var immagex: ArrayList<Bitmap>? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tow_vehicle)
        immagex = ArrayList()
        addMore.setOnClickListener { selectImage() }
        submit.setOnClickListener {


            var key: String = "VehicleNo"
            var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference(Vehicle.DB_TABLE_VEHICLE)

            val query = mDatabase.orderByChild(key).equalTo(vehicleNo.text.toString())
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {

                        for (snapshot in dataSnapshot.children) {

                            var bullet = snapshot.getValue<Vehicle>(Vehicle::class.java!!)
                            FineListSelect.reportedId = bullet!!.UserId!!
                            FineListSelect.vehicleNo = vehicleNo.text.toString()
                            FineListSelect.LicenceNo = ""

                            var user = Tow()
                            user.Latitude = (application as CustomApp).currentLocation!!.latitude
                            user.Longitude = (application as CustomApp).currentLocation!!.longitude
                            user.VehicleNo = vehicleNo.text.toString()
                            user.Comment = comment.text.toString()
                            user.DateTime = (application as CustomApp).getDateTime(Date())// Date().toString()
                            user.ReportedBy = FirebaseAuth.getInstance().currentUser!!.uid
                            var mDatabase: DatabaseReference =
                                FirebaseDatabase.getInstance().getReference(Tow.DB_TABLE_LICENCE)
                            var refer = mDatabase.push()
                            mDatabase.child(refer.key!!)
                                .setValue(user.getHashMap())
                                .addOnCompleteListener(OnCompleteListener {
                                    if (it.isSuccessful) {
                                        if (immagex!!.size > 0) {
                                            for (bitmp in immagex!!) {

                                                var path: String = ""
                                                path = Tow.DB_IMAGE
                                                var mStorage: StorageReference =
                                                    FirebaseStorage.getInstance()
                                                        .getReference(
                                                            path + "/" + refer.key!! + "_" + immagex!!.indexOf(
                                                                bitmp
                                                            ) + ".png"
                                                        )


                                                val baos = ByteArrayOutputStream()
                                                immagex!!.get(0).compress(Bitmap.CompressFormat.PNG, 50, baos)
                                                val b = baos.toByteArray()
                                                mStorage.putBytes(b)
                                                    .addOnSuccessListener(OnSuccessListener {
                                                        System.out.println("Success uploaded")

                                                    }).addOnFailureListener(OnFailureListener {
                                                        System.out.println("Success failed")
                                                    })
                                            }
                                        }

                                    } else {

                                    }
                                })


                        }

                    } else {
                        // Do stuff
                    }
                }


            })

        }

        pager!!.clipToPadding = false;
        pager.offscreenPageLimit = immagex!!.size
        pager.setPadding(20, 0, 20, 0);
        pagerAdapter = PhotoAdapter(supportFragmentManager, immagex!!)
        pager.adapter = pagerAdapter
        pager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(p0: Int) {

                }

            })


    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(
            getString(R.string.button_take_photo),
            getString(R.string.button_galley),
            getString(R.string.button_cancel)
        )
        val builder = AlertDialog.Builder(this, R.style.custom_dialog_theme)
        builder.setTitle(getString(R.string.title_profile_image))
        builder.setItems(items) { dialog, item ->
            val result = Utility.checkPermission(this)
            if (items[item] == getString(R.string.button_take_photo)) {
                //userChoosenTask="Take Photo";
                if (result)
                    callCamera()
            } else if (items[item] == getString(R.string.button_galley)) {
                //userChoosenTask="Choose from Library";
                if (result)
                    callGallery()
            } else if (items[item] == getString(R.string.button_cancel)) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun callCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PICK_CAMERA)
        }
    }

    private fun callGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.title_profile_image)), PICK_IMAGE)
    }

    class PhotoAdapter(fragmentManager: FragmentManager, private val movies: ArrayList<Bitmap>) :
        FragmentStatePagerAdapter(fragmentManager) {

        // 2
        override fun getItem(position: Int): Fragment {
            PhotoFragment.Bitmap = movies.get(position)
            return PhotoFragment()
        }

        // 3
        override fun getCount(): Int {
            return movies.size
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PICK_CAMERA || requestCode == PICK_IMAGE) && resultCode == RESULT_OK) {


            if (requestCode == PICK_CAMERA && data != null) {
                val extras = data.extras
                val selectedImage = extras!!.get("data") as Bitmap
                val baos = ByteArrayOutputStream()
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos)

                val b = baos.toByteArray()
                val sizevalue = b.size.toLong()
                if (sizevalue > 2048000) {
                    //  showToast(getString(R.string.error_image_size))
                } else {
                    immagex!!.add(selectedImage)
                    pagerAdapter!!.notifyDataSetChanged()
                }

            } else if (requestCode == PICK_IMAGE && data != null) {
                val extras = data.data
                if (extras != null) {
                    try {
                        val imageStream = contentResolver.openInputStream(extras)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        val baos = ByteArrayOutputStream()
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos)

                        val b = baos.toByteArray()
                        val file = File(getRealPathFromURI(extras)!!)
                        val fileNameinfo = file.getName()
                        val extension = fileNameinfo.substring(fileNameinfo.lastIndexOf(".") + 1, fileNameinfo.length)
                        val sizevalue = b.size.toLong()
                        if (sizevalue > 2048000) {
                            // showToast(getString(R.string.error_image_size))
                        } else {
                           // if (extension == "jpg" || extension == "png" || extension == "gif") {
                                immagex!!.add(selectedImage);
                                pagerAdapter!!.notifyDataSetChanged()
                          //  }
                            //else
                            // showToast(getString(R.string.error_image_type))
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }

    fun getRealPathFromURI(contentUri: Uri): String? {
        var path: String? = null
        val proj = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = contentResolver.query(contentUri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            path = cursor.getString(column_index)
        }
        cursor.close()
        return path
    }
}
