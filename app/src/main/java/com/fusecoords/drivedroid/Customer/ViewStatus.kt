package com.fusecoords.drivedroid.Customer

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.fusecoords.drivedroid.Authority.Receipt
import com.fusecoords.drivedroid.Authority.Violation
import com.fusecoords.drivedroid.R
import com.fusecoords.drivedroid.R.id.recylerType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.JsonObject
import com.payumoney.core.PayUmoneySdkInitializer
import com.payumoney.core.entity.TransactionResponse
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager
import kotlinx.android.synthetic.main.activity_view_status.*
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class ViewStatus : AppCompatActivity(), StatusAdapter.OnClickListener {
    lateinit var items: ArrayList<Violation>
    var licenceTypeAdapter: StatusAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_status)

        items = ArrayList()
        licenceTypeAdapter = StatusAdapter(items, this)
        recylerType.adapter = licenceTypeAdapter
        recylerType.layoutManager = LinearLayoutManager(this)
        var mDatabase: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(Receipt.DB_TABLE_LICENCE)

        mDatabase.child(FirebaseAuth.getInstance().uid!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    items.clear()
                    for (snapshot in dataSnapshot.children) {
                        var bullet = snapshot.getValue<Violation>(Violation::class.java!!)
                        bullet!!.Key = snapshot.key!!
                        items.add(bullet!!)
                    }
                    licenceTypeAdapter!!.notifyDataSetChanged()
                }
            }
        })

    }

    override fun OnClick(receipt: Violation) {

        makePayment(receipt)

    }

    fun makePayment(receipt: Violation) {

        val phone = "8882434664"
        val productName = receipt!!.Offence
        val firstName = "piyush"
        val txnId = receipt.Key
        val email = "piyush.jain@payu.in"
        //        "https://www.payumoney.com/mobileapp/payumoney/success.php
        val sUrl = "https://test.payumoney.com/mobileapp/payumoney/success.php"
        val fUrl = "https://test.payumoney.com/mobileapp/payumoney/failure.php"
        val udf1 = ""
        val udf2 = ""
        val udf3 = ""
        val udf4 = ""
        val udf5 = ""
        val isDebug = true
        val key = "rjQUPktU"
        val merchantId = "4934580"
        val builder = PayUmoneySdkInitializer.PaymentParam.Builder()

        var amount = java.lang.Double.parseDouble(receipt!!.TotalAmount)
        builder.setAmount(amount)
            .setTxnId(txnId)
            .setPhone(phone)
            .setProductName(productName)
            .setFirstName(firstName)
            .setEmail(email)
            .setsUrl(sUrl)
            .setfUrl(fUrl)
            .setUdf1(udf1)
            .setUdf2(udf2)
            .setUdf3(udf3)
            .setUdf4(udf4)
            .setUdf5(udf5)
            .setIsDebug(isDebug)
            .setKey(key)
            .setMerchantId(merchantId)

        val paymentParam = builder.build()


        val salt = "e5iIg1jwi8"
        val serverCalculatedHash = hashCal(
            key + "|" + txnId + "|" + amount + "|" + productName + "|"
                    + firstName + "|" + email + "|" + udf1 + "|" + udf2 + "|" + udf3 + "|" + udf4 + "|" + udf5 + "||||||" + salt
        )
        paymentParam.setMerchantHash(serverCalculatedHash)
        PayUmoneyFlowManager.startPayUMoneyFlow(
            paymentParam,
            this@ViewStatus,
            android.R.style.Animation_Activity, false
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code $requestCode resultcode $resultCode")
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK && data != null) {
            val transactionResponse =
                data!!.getParcelableExtra<TransactionResponse>(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE)

            if (transactionResponse != null && transactionResponse!!.getPayuResponse() != null) {

                if (transactionResponse!!.transactionStatus == TransactionResponse.TransactionStatus.SUCCESSFUL) {
                    //Success Transaction
                    val payuResponse = JSONObject(transactionResponse!!.getPayuResponse())
                    val taxId = payuResponse.optJSONObject("result").optString("txnid")

                    var mDatabase: DatabaseReference =
                        FirebaseDatabase.getInstance().getReference(Receipt.DB_TABLE_LICENCE)
                    val result = HashMap<String, Any>()
                    result.put("IsPaid", true)
                    mDatabase.child(FirebaseAuth.getInstance().uid!!).child(taxId).updateChildren(result)
                    for (item in items) {
                        if (item.Key.equals(taxId)) {
                            item.IsPaid = true
                        }
                    }
                    licenceTypeAdapter!!.notifyDataSetChanged()

                } else {

                    //Failure Transaction
                }

                // Response from Payumoney


                // Response from SURl and FURL
                val merchantResponse = transactionResponse!!.transactionDetails
            } else {

                Log.d(TAG, "Both objects are null!")
            }//            else if (resultModel != null && resultModel.getError() != null) {
            //                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            //            }
        }
    }

    companion object {

        val TAG = "PayUMoneySDK Sample"
        var receipt: Receipt? = null
        fun hashCal(str: String): String {
            val hashseq = str.toByteArray()
            val hexString = StringBuilder()
            try {
                val algorithm = MessageDigest.getInstance("SHA-512")
                algorithm.reset()
                algorithm.update(hashseq)
                val messageDigest = algorithm.digest()
                for (aMessageDigest in messageDigest) {
                    val hex = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    if (hex.length == 1) {
                        hexString.append("0")
                    }
                    hexString.append(hex)
                }
            } catch (ignored: NoSuchAlgorithmException) {
            }

            return hexString.toString()
        }
    }

}
