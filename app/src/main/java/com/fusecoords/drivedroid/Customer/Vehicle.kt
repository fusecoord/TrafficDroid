package com.fusecoords.drivedroid.Customer

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable


class Vehicle {
    companion object {
        val DB_TABLE_VEHICLE: String = "Vehicle"

    }

    constructor() {

    }

    var VehicleNo: String? = ""
    var RegNo: String? = ""
    var RegDate: String? = ""
    var Model: String? = ""
    var Class: String? = ""
    var ChasisNo: String? = ""
    var EngineNo: String? = ""
    var FuelType: String? = ""
    var RcExpiry: String? = ""
    var UserId: String? = ""


    fun getHashMap(): Any? {
        val jsonMap = Gson().fromJson<Any>(
            Gson().toJson(this),
            object : TypeToken<HashMap<String, Any>>() {}.type
        )
        return jsonMap
    }


}
