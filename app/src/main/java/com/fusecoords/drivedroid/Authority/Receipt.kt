package com.fusecoords.drivedroid.Authority

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Receipt {
    companion object {
        val DB_TABLE_LICENCE: String = "Violation"

    }

    constructor() {

    }

    var Offence: String = ""
    var Section: String = ""
    var TotalAmount: String = ""
    var LicenceNo: String = ""
    var VehicleNo: String = ""
    var FineId: Int = 0
    var Latitude: Double = 0.0
    var Longitude: Double = 0.0
    var Date: String = ""
    var UserId: String = ""
    var ReportedId: String = ""
    var PreviousViolationID: String = ""
    var IsPaid: Boolean = false
    fun getHashMap(): Any? {
        val jsonMap = Gson().fromJson<Any>(
            Gson().toJson(this),
            object : TypeToken<HashMap<String, Any>>() {}.type
        )
        return jsonMap
    }
}