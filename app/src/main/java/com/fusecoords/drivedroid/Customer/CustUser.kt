package com.fusecoords.drivedroid.Customer

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable


class CustUser {
    companion object {
        val DB_TABLE_CUST: String = "Customer"
        val DB_TABLE_USER: String = "Users"
        val DB_USER_PATH = DB_TABLE_USER + "/" + DB_TABLE_CUST
        val DB_IMAGE_PATH: String = "Images/" + DB_USER_PATH

        val DB_IMAGE_PUC: String = "Images/PUC"
        val DB_IMAGE_RC: String = "Images/RC"
        val DB_IMAGE_TAX: String = "Images/TAX"
        val DB_IMAGE_INS: String = "Images/INS"

    }

    constructor() {

    }

    var Email: String? = ""
    var Contact: String? = ""
    var FirstName: String? = ""
    var LastName: String? = ""

    var Parent: String? = ""
    var Dob: String? = ""
    var BloodGroup: String? = ""
    var Address: String? = ""
    var PinCode: String? = ""


    fun getHashMap(): Any? {
        val jsonMap = Gson().fromJson<Any>(
            Gson().toJson(this),
            object : TypeToken<HashMap<String, Any>>() {}.type
        )
        return jsonMap
    }
}
