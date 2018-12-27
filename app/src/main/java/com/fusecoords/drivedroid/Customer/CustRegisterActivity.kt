package com.fusecoords.drivedroid.Customer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.fusecoords.drivedroid.Authority.MainActivity
import com.fusecoords.drivedroid.Authority.ResetPasswordActivity
import com.fusecoords.drivedroid.Authority.User
import com.fusecoords.drivedroid.Authority.Utility
import com.fusecoords.drivedroid.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_cust_register.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class CustRegisterActivity : AppCompatActivity() {

    private var inputFirstName: EditText? = null
    private var inputLastName: EditText? = null
    private var inputConfirmPassword: EditText? = null

    private var inputEmail: EditText? = null
    private var inputPhone: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignIn: Button? = null
    private var btnSignUp: Button? = null
    private var btnResetPassword: Button? = null
    private var progressBar: ProgressBar? = null
    private var auth: FirebaseAuth? = null


    private var btnImagePick: ImageButton? = null
    var lengthbmp: Long? = 0
    var fileName: String? = ""
    var fileExtenionType: String? = ""
    var imageData: String? = ""
    private val PICK_IMAGE = 1000
    private val PICK_CAMERA = 1001
    var immagex: Bitmap? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cust_register)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()

        btnSignIn = findViewById(R.id.sign_in_button) as Button
        btnSignUp = findViewById(R.id.sign_up_button) as Button
        inputEmail = findViewById(R.id.email) as EditText
        inputPhone = findViewById(R.id.phone) as EditText
        inputPassword = findViewById(R.id.password) as EditText
        inputFirstName = findViewById(R.id.firstname) as EditText
        inputLastName = findViewById(R.id.lastname) as EditText
        inputConfirmPassword = findViewById(R.id.confirmpassword) as EditText
        progressBar = findViewById(R.id.progressBar) as ProgressBar
        btnResetPassword = findViewById(R.id.btn_reset_password) as Button
        btnImagePick = findViewById(R.id.img_profile) as ImageButton
        btnResetPassword!!.setOnClickListener {
            startActivity(
                Intent( this@CustRegisterActivity,
                    ResetPasswordActivity::class.java
                )
            )
        }
        btnImagePick!!.setOnClickListener { selectImage() }
        btnSignIn!!.setOnClickListener { finish() }

        btnSignUp!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim()
            val password = inputPassword!!.text.toString().trim()
            val contact = inputPhone!!.text.toString().trim()
            val firstname = inputFirstName!!.text.toString().trim()
            val lastname = inputLastName!!.text.toString().trim()


            val parent = fathername!!.text.toString().trim()
            val dob = dob!!.text.toString().trim()
            val bloodgroup = bloodgroup!!.text.toString().trim()
            val address = address!!.text.toString().trim()
            val pincode = pincode!!.text.toString().trim()


            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (password.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Password too short, enter minimum 6 characters!",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            } else {

                progressBar!!.visibility = View.VISIBLE
                //create user
                auth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@CustRegisterActivity) { task ->
                        Toast.makeText(
                            this@CustRegisterActivity,
                            "createUserWithEmail:onComplete:" + task.isSuccessful,
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar!!.visibility = View.GONE
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful) {
                            Toast.makeText(
                                this@CustRegisterActivity, "Authentication failed." + task.exception!!,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (immagex != null) {
                                var mStorage: StorageReference =
                                    FirebaseStorage.getInstance()
                                        .getReference(CustUser.DB_IMAGE_PATH + "/" + FirebaseAuth.getInstance().currentUser!!.uid + ".png")

                                val baos = ByteArrayOutputStream()
                                immagex!!.compress(Bitmap.CompressFormat.PNG, 90, baos)
                                val b = baos.toByteArray()
                                mStorage.putBytes(b)
                                    .addOnSuccessListener(OnSuccessListener {
                                        System.out.println("Success uploaded")
                                    }).addOnFailureListener(OnFailureListener {
                                        System.out.println("Success failed")
                                    })
                            }
                            var user = CustUser();
                            user.FirstName = firstname
                            user.LastName = lastname
                            user.Contact = contact
                            user.Email = email

                            user.Parent = parent
                            user.Dob = dob
                            user.BloodGroup = bloodgroup
                            user.Address = address
                            user.PinCode = pincode

                            var mDatabase: DatabaseReference =
                                FirebaseDatabase.getInstance().getReference(CustUser.DB_USER_PATH)
                            mDatabase.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .setValue(user.getHashMap())
                                .addOnCompleteListener(OnCompleteListener {
                                    if (it.isSuccessful) {
                                        startActivity(Intent(this@CustRegisterActivity, CustDashboard::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@CustRegisterActivity, "Register update failed.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        }
                    }
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
                    lengthbmp = sizevalue
                    fileExtenionType = extension
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

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }
}
