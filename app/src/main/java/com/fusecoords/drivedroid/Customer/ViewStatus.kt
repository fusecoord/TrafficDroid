package com.fusecoords.drivedroid.Customer

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.fusecoords.drivedroid.Authority.Receipt
import com.fusecoords.drivedroid.Authority.Violation
import com.fusecoords.drivedroid.CustomApp
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
import java.util.*
import android.R.attr.duration
import android.support.annotation.IntegerRes
import java.util.concurrent.TimeUnit
import com.sasidhar.smaps.payumoney.PayUMoney_Constants
import com.sasidhar.smaps.payumoney.Utils
import com.sasidhar.smaps.payumoney.Utils.generateHash
import com.sasidhar.smaps.payumoney.MakePaymentActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_payment.*


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

                        var dateAtIndex = (application as CustomApp).toDateTime(bullet.Date)

                        val diff = Date().getTime() - dateAtIndex.getTime()
                        val diffInDays = TimeUnit.MILLISECONDS.toDays(diff)
                        if (diffInDays > 7) {
                            if (diffInDays > 7)
                                bullet!!.LateFees = 100
                            else if (diffInDays > 14)
                                bullet!!.LateFees = 200
                            else if (diffInDays > 21)
                                bullet!!.LateFees = 300
                            else if (diffInDays > 28)
                                bullet!!.LateFees = 400
                            bullet!!.TotalAmount = bullet!!.TotalAmount +
                                    bullet!!.LateFees
                        }
                        items.add(bullet!!)
                    }
                    licenceTypeAdapter!!.notifyDataSetChanged()
                }
            }
        })

    }

    var globalReceipt: Violation? = null
    override fun OnClick(receipt: Violation) {

        makePayment(receipt)
        globalReceipt = receipt
    }

    fun makePayment(receipt: Violation) {

        val phone = "8882434664"
        val productName = receipt!!.Offence
        val firstName = "Auth"
        val txnId = receipt.Key
        val email = "auth@gmail.com"
        //        "https://www.payumoney.com/mobileapp/payumoney/success.php
        val sUrl = "https://test.payumoney.com/mobileapp/payumoney/success.php"
        val fUrl = "https://test.payumoney.com/mobileapp/payumoney/failure.php"
        val udf1 = receipt.LateFees
        val udf2 = ""
        val udf3 = ""
        val udf4 = ""
        val udf5 = ""
        val isDebug = true
        val key = "rjQUPktU"
        val merchantId = "4934580"
        val builder = PayUmoneySdkInitializer.PaymentParam.Builder()
        val salt = "e5iIg1jwi8"
        val serverCalculatedHash = hashCal(
            key + "|" + txnId + "|" + amount + "|" + productName + "|"
                    + firstName + "|" + email + "|" + udf1 + "|" + udf2 + "|" + udf3 + "|" + udf4 + "|" + udf5 + "||||||" + salt
        )
        /* var amount = java.lang.Double.parseDouble(receipt!!.TotalAmount.toString())
         builder.setAmount(amount)
             .setTxnId(txnId)
             .setPhone(phone)
             .setProductName(productName)
             .setFirstName(firstName)
             .setEmail(email)
             .setsUrl(sUrl)
             .setfUrl(fUrl)
             .setUdf1("" + udf1)
             .setUdf2(udf2)
             .setUdf3(udf3)
             .setUdf4(udf4)
             .setUdf5(udf5)
             .setIsDebug(isDebug)
             .setKey(key)
             .setMerchantId(merchantId)

         val paymentParam = builder.build()

         paymentParam.setMerchantHash(serverCalculatedHash)
         PayUmoneyFlowManager.startPayUMoneyFlow(
             paymentParam,
             this@ViewStatus,
             android.R.style.Animation_Activity, false
         )*/

        val params = HashMap<String, String>()
        params.put(PayUMoney_Constants.KEY, key) // Get merchant key from PayU Money Account
        params.put(PayUMoney_Constants.TXN_ID, txnId)
        params.put(PayUMoney_Constants.AMOUNT, "" + receipt.TotalAmount)
        params.put(PayUMoney_Constants.PRODUCT_INFO, productName)
        params.put(PayUMoney_Constants.FIRST_NAME, firstName)
        params.put(PayUMoney_Constants.EMAIL, email)
        params.put(PayUMoney_Constants.PHONE, phone)
        params.put("isdebug", "1")
        params.put(PayUMoney_Constants.SURL, sUrl)
        params.put(PayUMoney_Constants.FURL, fUrl)


// User defined fields are optional (pass empty string)
        params.put(PayUMoney_Constants.UDF1, "" + receipt.LateFees)
        params.put(PayUMoney_Constants.UDF2, "")
        params.put(PayUMoney_Constants.UDF3, "")
        params.put(PayUMoney_Constants.UDF4, "")
        params.put(PayUMoney_Constants.UDF5, "")


// generate hash by passing params and salt
        val hash = Utils.generateHash(params, salt) // Get Salt from PayU Money Account
        params.put(PayUMoney_Constants.HASH, hash)


// SERVICE PROVIDER VALUE IS ALWAYS "payu_paisa".
        params.put(PayUMoney_Constants.SERVICE_PROVIDER, "payu_paisa")
        val intent = Intent(this, MakePaymentActivity::class.java)
        intent.putExtra(PayUMoney_Constants.ENVIRONMENT, PayUMoney_Constants.ENV_DEV)
        intent.putExtra(PayUMoney_Constants.PARAMS, params)
        startActivityForResult(intent, PayUMoney_Constants.PAYMENT_REQUEST);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code $requestCode resultcode $resultCode")
        /* if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK && data != null) {
             val transactionResponse =
                 data!!.getParcelableExtra<TransactionResponse>(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE)

             if (transactionResponse != null && transactionResponse!!.getPayuResponse() != null) {

                 if (transactionResponse!!.transactionStatus == TransactionResponse.TransactionStatus.SUCCESSFUL) {
                     //Success Transaction
                     val payuResponse = JSONObject(transactionResponse!!.getPayuResponse())
                     val taxId = payuResponse.optJSONObject("result").optString("txnid")
                     val udf1 = payuResponse.optJSONObject("result").optString("udf1")
                     var mDatabase: DatabaseReference =
                         FirebaseDatabase.getInstance().getReference(Receipt.DB_TABLE_LICENCE)
                     val result = HashMap<String, Any>()
                     result.put("LateFees", udf1)
                     result.put("IsPaid", true)
                     result.put("PaidDate", (application as CustomApp).getDateTime(Date()))
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
         }*/
        if (requestCode === PayUMoney_Constants.PAYMENT_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(this@ViewStatus, "Payment Success.", Toast.LENGTH_SHORT).show()

                    val taxId = globalReceipt!!.Key
                    val udf1 = globalReceipt!!.LateFees //payuResponse.optJSONObject("result").optString("udf1")
                    var mDatabase: DatabaseReference =
                        FirebaseDatabase.getInstance().getReference(Receipt.DB_TABLE_LICENCE)
                    val result = HashMap<String, Any>()
                    result.put("LateFees", udf1)
                    result.put("IsPaid", true)
                    result.put("PaidDate", (application as CustomApp).getDateTime(Date()))
                    mDatabase.child(FirebaseAuth.getInstance().uid!!).child(taxId).updateChildren(result)
                    for (item in items) {
                        if (item.Key.equals(taxId)) {
                            item.IsPaid = true
                        }
                    }
                    licenceTypeAdapter!!.notifyDataSetChanged()

                }
                Activity.RESULT_CANCELED -> Toast.makeText(
                    this@ViewStatus,
                    "Payment Cancelled | Failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

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
