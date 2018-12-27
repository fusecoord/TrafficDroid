package com.fusecoords.drivedroid.Customer

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.fusecoords.drivedroid.Authority.Receipt
import com.fusecoords.drivedroid.R
import android.provider.SyncStateContract.Helpers.update
import com.fusecoords.drivedroid.Authority.Tow
import com.fusecoords.drivedroid.Authority.Violation
import com.payumoney.core.entity.PaymentEntity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class TowAdapter(val items: ArrayList<Tow>, val context: Context) : RecyclerView.Adapter<TowHolder>() {

    interface OnClickListener {
        fun OnClick(receipt: Tow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TowHolder {
        return TowHolder(LayoutInflater.from(context).inflate(R.layout.item_tow, parent, false))
    }

    override fun onBindViewHolder(holder: TowHolder, position: Int) {
        holder.date.text = items.get(
            position
        ).DateTime

        holder.comment.text = items.get(
            position
        ).Comment

        holder.contact.text = items.get(
            position
        ).Contact
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class TowHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var date: TextView = view.findViewById(R.id.date)
    var comment: TextView = view.findViewById(R.id.comment)
    var contact: TextView = view.findViewById(R.id.contact)
}