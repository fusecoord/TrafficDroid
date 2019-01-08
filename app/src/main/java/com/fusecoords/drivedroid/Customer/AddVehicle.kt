package com.fusecoords.drivedroid.Customer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.fusecoords.drivedroid.Authority.MainActivity
import com.fusecoords.drivedroid.Authority.User
import com.fusecoords.drivedroid.Authority.Utility
import com.fusecoords.drivedroid.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_vehicle.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class AddVehicle : AppCompatActivity() {
    var immagex: Bitmap? = null;


    private val PICK_IMAGE = 1000
    private val PICK_CAMERA = 1001
    lateinit var items: ArrayList<LicenceType>
    var licenceTypeAdapter: LicenceTypeAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle)
        items = ArrayList()
        licenceTypeAdapter = LicenceTypeAdapter(items, this)
        recylerType.adapter = licenceTypeAdapter
        recylerType.layoutManager = LinearLayoutManager(this)
        addType.setOnClickListener {
            if (input_item.visibility == View.VISIBLE) {
                input_item.visibility = View.GONE
            } else {
                input_item.visibility = View.VISIBLE
            }
        }
        img_profile.setOnClickListener { selectImage() }
        save_item.setOnClickListener {
            var licenceType = LicenceType()
            licenceType.LicenceType = type.text.toString()
            licenceType.LicenceDOI = issueDate.text.toString()
            items.add(licenceType)
            licenceTypeAdapter!!.notifyDataSetChanged()
        }
        save!!.setOnClickListener(View.OnClickListener {


            val vehicleNo = vehicleNo!!.text.toString().trim()
            val regNo = regNo!!.text.toString().trim()
            val regdate = regDate!!.text.toString().trim()
            val model = model!!.text.toString().trim()
            val classv = classv!!.text.toString().trim()

            val chasis = chasis!!.text.toString().trim()
            val engNo = engineNo!!.text.toString().trim()
            val fuelType = fuelType!!.text.toString().trim()
            val rcexpiry = rcexpiry!!.text.toString().trim()



            if (TextUtils.isEmpty(vehicleNo)) {
                Toast.makeText(applicationContext, "Enter vehicle no!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(regNo)) {
                Toast.makeText(applicationContext, "Enter reg no!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {


                var user = Vehicle();

                user.VehicleNo = vehicleNo
                user.RegNo = regNo
                user.RegDate = regdate
                user.Model = model
                user.Class = classv
                user.ChasisNo = chasis
                user.EngineNo = engNo
                user.FuelType = fuelType
                user.RcExpiry = rcexpiry

                user.UserId = FirebaseAuth.getInstance().currentUser!!.uid


                var mDatabase: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference(Vehicle.DB_TABLE_VEHICLE)

                val query = mDatabase.orderByChild("VehicleNo").equalTo(vehicleNo)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            var ref = mDatabase.push();
                            mDatabase.child(ref.key!!)
                                .setValue(user.getHashMap())
                                .addOnCompleteListener(OnCompleteListener {
                                    if (it.isSuccessful) {

                                        if (immagex != null) {
                                            var mStorage: StorageReference =
                                                FirebaseStorage.getInstance()
                                                    .getReference(Licence.DB_IMAGE_PATH + "/" + Licence.DB_TABLE_LICENCE + "/" + ref.key!! + ".png")

                                            val baos = ByteArrayOutputStream()
                                            immagex!!.compress(Bitmap.CompressFormat.PNG, 90, baos)
                                            val b = baos.toByteArray()
                                            mStorage.putBytes(b)
                                                .addOnSuccessListener(OnSuccessListener {
                                                    startActivity(Intent(this@AddVehicle, CustDashboard::class.java))
                                                    finish()
                                                }).addOnFailureListener(OnFailureListener {
                                                    System.out.println("Success failed")
                                                })


                                        }


                                    } else {
                                        Toast.makeText(
                                            this@AddVehicle, "Register update failed.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        }
                    }

                })

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PICK_CAMERA || requestCode == PICK_IMAGE) && resultCode == RESULT_OK) {


            if (requestCode == PICK_CAMERA && data != null) {
                val extras = data.extras
                val selectedImage = extras!!.get("data") as Bitmap
                val baos = ByteArrayOutputStream()
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                immagex = selectedImage;
                val b = baos.toByteArray()
                val extension = "jpg"
                val sizevalue = b.size.toLong()

                if (sizevalue > 2048000) {
                    //  showToast(getString(R.string.error_image_size))

                } else {

                    img_profile.setImageBitmap(selectedImage)
                }

            } else if (requestCode == PICK_IMAGE && data != null) {
                val extras = data.data
                if (extras != null) {
                    try {
                        val imageStream = contentResolver.openInputStream(extras)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        val baos = ByteArrayOutputStream()
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                        immagex = selectedImage;
                        val b = baos.toByteArray()
                        val file = File(getRealPathFromURI(extras)!!)
                        val fileNameinfo = file.getName()
                        val extension = fileNameinfo.substring(fileNameinfo.lastIndexOf(".") + 1, fileNameinfo.length)
                        val sizevalue = b.size.toLong()
                        if (sizevalue > 2048000) {
                            // showToast(getString(R.string.error_image_size))

                        } else {
                         //   if (extension == "jpg" || extension == "png" || extension == "gif") {
                                img_profile.setImageBitmap(selectedImage)
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

    override fun onResume() {
        super.onResume()

    }
}
