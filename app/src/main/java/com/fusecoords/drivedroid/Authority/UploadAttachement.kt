package com.fusecoords.drivedroid.Authority

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.widget.ImageButton
import com.fusecoords.drivedroid.Customer.CustUser
import com.fusecoords.drivedroid.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_upload_attachement.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class UploadAttachement : AppCompatActivity() {
    private var btnImagePick: ImageButton? = null


    private val PICK_IMAGE = 1000
    private val PICK_CAMERA = 1001
    var immagex: Bitmap? = null;

    companion object {
        var flow: Int = 0
        var vehicleNo: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_attachement)
        btnImagePick = findViewById(R.id.img_profile) as ImageButton
        btnImagePick!!.setOnClickListener { selectImage() }

        var path: String = ""
        if (Attachement.flow == 1)
            path = CustUser.DB_IMAGE_RC
        else if (Attachement.flow == 2)
            path = CustUser.DB_IMAGE_TAX
        else if (Attachement.flow == 3)
            path = CustUser.DB_IMAGE_PUC
        else if (Attachement.flow == 4)
            path = CustUser.DB_IMAGE_INS
        var mStorage: StorageReference = FirebaseStorage.getInstance().getReference()


        mStorage.child(path + "/" + Attachement.vehicleNo + ".png").getDownloadUrl()
            .addOnSuccessListener(OnSuccessListener<Any> { uri ->
                System.out.println("Pass" + uri)
                Picasso.get().load(uri.toString()).into(img_profile)
            }).addOnFailureListener(OnFailureListener {
                // Handle any errors
                System.out.println("Failuare")
            })

        uploadAttach.setOnClickListener {
            if (immagex != null) {
                var mStorage: StorageReference = FirebaseStorage.getInstance().getReference(path + "/" + Attachement.vehicleNo + ".png")
                val baos = ByteArrayOutputStream()
                immagex!!.compress(Bitmap.CompressFormat.PNG, 50, baos)
                val b = baos.toByteArray()
                mStorage.putBytes(b)
                    .addOnSuccessListener(OnSuccessListener {
                        System.out.println("Success uploaded")
                        finish()
                    }).addOnFailureListener(OnFailureListener {
                        System.out.println("Success failed")
                    })
            }
        }

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
                            if (extension == "jpg" || extension == "png" || extension == "gif") {
                                img_profile.setImageBitmap(selectedImage)
                            }
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
