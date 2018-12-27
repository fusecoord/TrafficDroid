package com.fusecoords.drivedroid.Customer

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable


class LicenceType {
    companion object {

        val DB_TABLE_LICENCE_TYPE: String = "LicenceType"

    }

    constructor() {

    }

    var LicenceId: String? = ""
    var LicenceType: String? = ""
    var LicenceDOI: String? = ""


    fun getHashMap(): Any? {
        val jsonMap = Gson().fromJson<Any>(
            Gson().toJson(this),
            object : TypeToken<HashMap<String, Any>>() {}.type
        )
        return jsonMap
    }


}
