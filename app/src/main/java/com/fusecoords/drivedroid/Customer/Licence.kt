package com.fusecoords.drivedroid.Customer

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable


class Licence {
    companion object {
        val DB_TABLE_LICENCE: String = "Licence"
        val DB_TABLE_LICENCE_TYPE: String = "LicenceType"
        val DB_IMAGE_PATH: String = "Images"
        val DB_SIGN_PATH: String = "Sign"
    }

    constructor() {

    }

    var LicenceNo: String? = ""
    var Doi: String? = ""
    var Doe: String? = ""
    var Dld: String? = ""
    var UserId: String? = ""
    var Auth: String? = ""
    var licenceType: ArrayList<LicenceType>? = null

    fun getHashMap(): Any? {
        val jsonMap = Gson().fromJson<Any>(
            Gson().toJson(this),
            object : TypeToken<HashMap<String, Any>>() {}.type
        )
        return jsonMap
    }


}
