package com.fusecoords.drivedroid.Customer

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable


class Division {
    companion object {
        val DB_TABLE_LICENCE: String = "Division"

    }

    constructor() {

    }

    var Address: String? = ""
    var Contact: Long? = 0
    var Designation: String? = ""
    var Divison_Name: String? = ""
    var Email: String? = ""
    var Id: Int? = 0
    var Landline: String? = ""
    var Name: String? = ""

    fun getHashMap(): Any? {
        val jsonMap = Gson().fromJson<Any>(
            Gson().toJson(this),
            object : TypeToken<HashMap<String, Any>>() {}.type
        )
        return jsonMap
    }


}
