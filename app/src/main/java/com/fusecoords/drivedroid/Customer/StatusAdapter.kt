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
import com.fusecoords.drivedroid.Authority.Violation
import com.payumoney.core.entity.PaymentEntity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class StatusAdapter(val items: ArrayList<Violation>, val context: Context) : RecyclerView.Adapter<StatusHolder>() {

    interface OnClickListener {
        fun OnClick(receipt: Violation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusHolder {
        return StatusHolder(LayoutInflater.from(context).inflate(R.layout.item_status, parent, false))
    }

    override fun onBindViewHolder(holder: StatusHolder, position: Int) {
        holder.date.setText(items.get(position).Date)
        holder.offence.setText(items.get(position).Offence)
        holder.law.setText(items.get(position).Section)
        if (items.get(position).LateFees.equals(0))
            holder.price.setText("Rs. " + items.get(position).TotalAmount)
        else
            holder.price.setText(
                "Rs. " + items.get(position).TotalAmount + " \n(Including Late Fees: +${items.get(
                    position
                ).LateFees})"
            )
        if (items.get(position).IsPaid) {
            holder.paid.setText("Paid")
            holder.paid.setOnClickListener { }
        } else {
            holder.paid.setOnClickListener {
                var receipt = items.get(position)
                var pm = context as OnClickListener
                pm.OnClick(receipt)

            }

            holder.paid.setText("UnPaid")
        }
        if (!items.get(0).VehicleNo.equals(""))
            holder.fineON.text = "Fine to be paid for Vehicle No : " + items.get(0).VehicleNo
        else if (!items.get(0).LicenceNo.equals(""))
            holder.fineON.text = "Fine to be paid for Licence No : " + items.get(0).LicenceNo

    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class StatusHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var date: TextView = view.findViewById(R.id.date)
    var offence: TextView = view.findViewById(R.id.offence)
    var law: TextView = view.findViewById(R.id.law)
    var price: TextView = view.findViewById(R.id.price)
    var paid: Button = view.findViewById(R.id.paid)
    var fineON: TextView = view.findViewById(R.id.fineOn)
}