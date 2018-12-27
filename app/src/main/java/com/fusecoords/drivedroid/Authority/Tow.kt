package com.fusecoords.drivedroid.Authority

import com.fusecoords.drivedroid.Customer.VehicleHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Tow {
    companion object {
        val DB_TABLE_LICENCE: String = "TowVehicle"
        val DB_IMAGE : String = "Images/TowVehicle"
    }

    constructor() {

    }
    var Contact: String = ""
    var VehicleNo: String = ""
    var DateTime: String = ""
    var ReportedBy: String = ""
    var Address: String = ""
    var Comment: String = ""
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