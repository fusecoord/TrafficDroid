package com.fusecoords.drivedroid.Authority

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Fine {
    companion object {
        val DB_TABLE_LICENCE: String = "Fine"

    }

    constructor() {

    }

    var Penalty: Int = 0
    var Sr_No: Int = 0
    var Offence: String = ""
    var Section: String = ""
    var isChecked: Boolean = false
    var violationList: ArrayList<Violation> = ArrayList<Violation>()
    fun getHashMap(): Any? {
        val jsonMap = Gson().fromJson<Any>(
            Gson().toJson(this),
            object : TypeToken<HashMap<String, Any>>() {}.type
        )
        return jsonMap
    }
}