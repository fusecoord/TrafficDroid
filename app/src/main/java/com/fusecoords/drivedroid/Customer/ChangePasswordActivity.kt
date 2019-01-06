package com.fusecoords.drivedroid.Customer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.fusecoords.drivedroid.R

import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.OnCompleteListener

import com.google.android.gms.tasks.Task


class ChangePasswordActivity : AppCompatActivity() {

    private var inputpass: EditText? = null
    private var inputrepass: EditText? = null
    private var btnReset: Button? = null

    private var auth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        inputpass = findViewById(R.id.pass) as EditText
        inputrepass = findViewById(R.id.repass) as EditText
        btnReset = findViewById(R.id.btn_reset_password) as Button

        progressBar = findViewById(R.id.progressBar) as ProgressBar



        btnReset!!.setOnClickListener(View.OnClickListener {
            val email = inputpass!!.text.toString().trim { it <= ' ' }
            val repass = inputrepass!!.text.toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(application, "Enter your password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (email.length < 6) {
                Toast.makeText(application, "Password too short, enter minimum 6 characters", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(repass)) {
                Toast.makeText(application, "Enter your confirm password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (!email.equals(repass)) {
                Toast.makeText(application, "Password doesnt matches", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar!!.visibility = View.VISIBLE
            var user = FirebaseAuth.getInstance().getCurrentUser();
            user!!.updatePassword(email.trim()).addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(task: Task<Void>) {
                    if (task.isSuccessful()) {
                        Toast.makeText(this@ChangePasswordActivity, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show()
                        progressBar!!.setVisibility(View.GONE)
                        finish()
                    } else {
                        Toast.makeText(this@ChangePasswordActivity, "Failed to update password!", Toast.LENGTH_SHORT).show()
                        progressBar!!.setVisibility(View.GONE)
                    }
                }
            })

        })
    }

}
