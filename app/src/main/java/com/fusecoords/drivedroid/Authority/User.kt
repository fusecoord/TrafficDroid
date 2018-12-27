package com.fusecoords.drivedroid.Authority

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable


class User {
    companion object {
        val DB_TABLE_CUST: String = "Authority"
        val DB_TABLE_USER: String = "Users"
        val DB_USER_PATH = DB_TABLE_USER + "/" + DB_TABLE_CUST
        val DB_IMAGE_PATH: String = "Images/" + DB_USER_PATH



    }
    constructor() {

    }
    internal var Email: String? = ""
    internal var Contact: String? = ""
    internal var FirstName: String? = ""
    internal var LastName: String? = ""


    fun getHashMap(): Any? {
        val jsonMap = Gson().fromJson<Any>(
            Gson().toJson(this),
            object : TypeToken<HashMap<String, Any>>() {}.type
        )
        return jsonMap
    }
}
