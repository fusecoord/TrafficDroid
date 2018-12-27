package com.fusecoords.drivedroid.Authority

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Alert {
    companion object {
        val DB_TABLE_LICENCE: String = "Alert"

    }

    constructor() {

    }

    var Note: String = ""
    var Date: String = ""
    var DateTime: String = ""
    var ReportedBy: String = ""
    var Address: String = ""
    var Latitude: Double = 0.0
    var Longitude: Double = 0.0


    fun getHashMap(): Any? {
        val jsonMap = Gson().fromJson<Any>(
            Gson().toJson(this),
            object : TypeToken<HashMap<String, Any>>() {}.type
        )
        return jsonMap
    }
}