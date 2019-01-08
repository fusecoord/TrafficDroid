package com.fusecoords.drivedroid.Authority

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.google.firebase.auth.FirebaseAuth
import android.widget.*

import com.google.zxing.integration.android.IntentIntegrator
import android.widget.Toast
import com.fusecoords.drivedroid.Customer.DivisonList
import com.fusecoords.drivedroid.Customer.RtoList
import com.fusecoords.drivedroid.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var btnUserInfo: Button? = null
    private var btnChargeFine: Button? = null

    private var btnChangeEmail: Button? = null
    private var btnChangePassword: Button? = null
    private var btnSendResetEmail: Button? = null
    private var btnRemoveUser: Button? = null
    private var changeEmail: Button? = null
    private var changePassword: Button? = null
    private var sendEmail: Button? = null
    private var remove: Button? = null
    private var signOut: ImageView? = null

    private var oldEmail: EditText? = null
    private var newEmail: EditText? = null
    private var password: EditText? = null
    private var newPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        //gps data
        //check history
        //toll booth
        //alert systemk to nearyby police
        //duplicate licence


        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
        btnUserInfo = findViewById(R.id.readInfo) as Button
        btnChargeFine = findViewById(R.id.chargeFine) as Button

        btnChangeEmail = findViewById(R.id.change_email_button) as Button
        btnChangePassword = findViewById(R.id.change_password_button) as Button
        btnSendResetEmail = findViewById(R.id.sending_pass_reset_button) as Button
        btnRemoveUser = findViewById(R.id.remove_user_button) as Button
        changeEmail = findViewById(R.id.changeEmail) as Button
        changePassword = findViewById(R.id.changePass) as Button
        sendEmail = findViewById(R.id.send) as Button
        remove = findViewById(R.id.remove) as Button
        signOut = findViewById(R.id.sign_out) as ImageView

        oldEmail = findViewById(R.id.old_email) as EditText
        newEmail = findViewById(R.id.new_email) as EditText
        password = findViewById(R.id.password) as EditText
        newPassword = findViewById(R.id.newPassword) as EditText

        oldEmail!!.visibility = View.GONE
        newEmail!!.visibility = View.GONE
        password!!.visibility = View.GONE
        newPassword!!.visibility = View.GONE
        changeEmail!!.visibility = View.GONE
        changePassword!!.visibility = View.GONE
        sendEmail!!.visibility = View.GONE
        remove!!.visibility = View.GONE

        progressBar = findViewById(R.id.progressBar) as ProgressBar

        if (progressBar != null) {
            progressBar!!.visibility = View.GONE
        }

        btnChargeFine!!.setOnClickListener {
            PayFine.flow = 1
            startActivity(Intent(this@MainActivity, PayFine::class.java))
        }
        btnUserInfo!!.setOnClickListener {
            PayFine.flow = 2
            startActivity(Intent(this@MainActivity, PayFine::class.java))
        }
        history.setOnClickListener {
            PayFine.flow = 3
            startActivity(Intent(this@MainActivity, PayFine::class.java))
        }
        sendAlert.setOnClickListener {
            startActivity(Intent(this@MainActivity, SendAlert::class.java))
        }
        towVehicle.setOnClickListener {
            startActivity(Intent(this@MainActivity, TowVehicle::class.java))
        }
        traffic_division.setOnClickListener {
            startActivity(Intent(this@MainActivity, DivisonList::class.java))
        }
        traffic_rto.setOnClickListener {
            startActivity(Intent(this@MainActivity, RtoList::class.java))
        }

        signOut!!.setOnClickListener {
            signOut()

        }


    }

    //sign out method
    fun signOut() {
        auth!!.signOut()
        finish()
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }

    public override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }


//        btnChangeEmail!!.setOnClickListener {
//            oldEmail!!.visibility = View.GONE
//            newEmail!!.visibility = View.VISIBLE
//            password!!.visibility = View.GONE
//            newPassword!!.visibility = View.GONE
//            changeEmail!!.visibility = View.VISIBLE
//            changePassword!!.visibility = View.GONE
//            sendEmail!!.visibility = View.GONE
//            remove!!.visibility = View.GONE
//        }
//
//        changeEmail!!.setOnClickListener {
//            progressBar!!.visibility = View.VISIBLE
//            if (user != null && newEmail!!.text.toString().trim { it <= ' ' } != "") {
//                user.updateEmail(newEmail!!.text.toString().trim { it <= ' ' })
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Toast.makeText(
//                                this@MainActivity,
//                                "Email address is updated. Please sign in with new email id!",
//                                Toast.LENGTH_LONG
//                            ).show()
//                            signOut()
//                            progressBar!!.visibility = View.GONE
//                        } else {
//                            Toast.makeText(this@MainActivity, "Failed to update email!", Toast.LENGTH_LONG).show()
//                            progressBar!!.visibility = View.GONE
//                        }
//                    }
//            } else if (newEmail!!.text.toString().trim { it <= ' ' } == "") {
//                newEmail!!.error = "Enter email"
//                progressBar!!.visibility = View.GONE
//            }
//        }

//        btnChangePassword!!.setOnClickListener {
//            oldEmail!!.visibility = View.GONE
//            newEmail!!.visibility = View.GONE
//            password!!.visibility = View.GONE
//            newPassword!!.visibility = View.VISIBLE
//            changeEmail!!.visibility = View.GONE
//            changePassword!!.visibility = View.VISIBLE
//            sendEmail!!.visibility = View.GONE
//            remove!!.visibility = View.GONE
//        }

//        changePassword!!.setOnClickListener {
//            progressBar!!.visibility = View.VISIBLE
//            if (user != null && newPassword!!.text.toString().trim { it <= ' ' } != "") {
//                if (newPassword!!.text.toString().trim { it <= ' ' }.length < 6) {
//                    newPassword!!.error = "Password too short, enter minimum 6 characters"
//                    progressBar!!.visibility = View.GONE
//                } else {
//                    user.updatePassword(newPassword!!.text.toString().trim { it <= ' ' })
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Toast.makeText(
//                                    this@MainActivity,
//                                    "Password is updated, sign in with new password!",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                signOut()
//                                progressBar!!.visibility = View.GONE
//                            } else {
//                                Toast.makeText(this@MainActivity, "Failed to update password!", Toast.LENGTH_SHORT)
//                                    .show()
//                                progressBar!!.visibility = View.GONE
//                            }
//                        }
//                }
//            } else if (newPassword!!.text.toString().trim { it <= ' ' } == "") {
//                newPassword!!.error = "Enter password"
//                progressBar!!.visibility = View.GONE
//            }
//        }

//        btnSendResetEmail!!.setOnClickListener {
//            oldEmail!!.visibility = View.VISIBLE
//            newEmail!!.visibility = View.GONE
//            password!!.visibility = View.GONE
//            newPassword!!.visibility = View.GONE
//            changeEmail!!.visibility = View.GONE
//            changePassword!!.visibility = View.GONE
//            sendEmail!!.visibility = View.VISIBLE
//            remove!!.visibility = View.GONE
//        }

//        sendEmail!!.setOnClickListener {
//            progressBar!!.visibility = View.VISIBLE
//            if (oldEmail!!.text.toString().trim { it <= ' ' } != "") {
//                auth!!.sendPasswordResetEmail(oldEmail!!.text.toString().trim { it <= ' ' })
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Toast.makeText(this@MainActivity, "Reset password email is sent!", Toast.LENGTH_SHORT)
//                                .show()
//                            progressBar!!.visibility = View.GONE
//                        } else {
//                            Toast.makeText(this@MainActivity, "Failed to send reset email!", Toast.LENGTH_SHORT).show()
//                            progressBar!!.visibility = View.GONE
//                        }
//                    }
//            } else {
//                oldEmail!!.error = "Enter email"
//                progressBar!!.visibility = View.GONE
//            }
//        }
}