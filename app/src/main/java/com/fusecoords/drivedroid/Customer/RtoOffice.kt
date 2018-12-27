package com.fusecoords.drivedroid.Customer

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable


class RtoOffice {
    companion object {
        val DB_TABLE_LICENCE: String = "Office"

    }

    constructor() {

    }

    var Address: String? = ""
    var Code: String? = ""
    var Email: String? = ""
    var Fax: String? = ""
    var Offices: String? = ""
    var Sr_No: Int? = 0
    var STDCode: String? = ""
     var PhoneNo: String? = ""

    fun getHashMap(): Any? {
        val jsonMap = Gson().fromJson<Any>(
            Gson().toJson(this),
            object : TypeToken<HashMap<String, Any>>() {}.type
        )
        return jsonMap
    }


}
