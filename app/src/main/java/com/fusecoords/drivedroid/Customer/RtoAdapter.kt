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
import com.fusecoords.drivedroid.R

class RtoAdapter(var items: ArrayList<RtoOffice>, val context: Context) : RecyclerView.Adapter<OfficeHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfficeHolder {
        return OfficeHolder(LayoutInflater.from(context).inflate(R.layout.item_rto_office, parent, false))
    }

    override fun onBindViewHolder(holder: OfficeHolder, position: Int) {
        holder.offices.text = items.get(position).Offices
        holder.email.text = items.get(position).Email

        holder.contact.text = "(" + items.get(position).STDCode + ") " +
                (items.get(position).PhoneNo!!.replace(",", ", "))
        holder.code.text = items.get(position).Code

    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
    fun filterList(filterdNames: ArrayList<RtoOffice>) {
        items = filterdNames
        notifyDataSetChanged()
    }
}

class OfficeHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var offices: TextView = view.findViewById(R.id.offices)
    var email: TextView = view.findViewById(R.id.email)
    var contact: TextView = view.findViewById(R.id.contact)
    var code: TextView = view.findViewById(R.id.Code)


}