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

class DivisionAdapter(var items: ArrayList<Division>, val context: Context) : RecyclerView.Adapter<DivisionHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivisionHolder {
        return DivisionHolder(LayoutInflater.from(context).inflate(R.layout.item_division, parent, false))
    }

    override fun onBindViewHolder(holder: DivisionHolder, position: Int) {
        holder.name.text = items.get(position).Name

        holder.division.text = items.get(position).Divison_Name
        holder.email.text = items.get(position).Email
        holder.contact.text = "" + items.get(position).Contact
        holder.designation.text = items.get(position).Designation
        if (items.get(position).Address.equals("")) {
            holder.map.visibility = View.GONE
        } else
            holder.map.visibility = View.VISIBLE
        holder.map.setOnClickListener {
            AddressLocation.Address =   items.get(position).Address!!
            context.startActivity(Intent(context, AddressLocation::class.java))
        }
    }

    fun filterList(filterdNames: ArrayList<Division>) {
        items = filterdNames
        notifyDataSetChanged()
    }
    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class DivisionHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var name: TextView = view.findViewById(R.id.name)
    var contact: TextView = view.findViewById(R.id.contact)
    var division: TextView = view.findViewById(R.id.division)
    var designation: TextView = view.findViewById(R.id.Desigantion)
    var email: TextView = view.findViewById(R.id.email)
    var map: Button = view.findViewById(R.id.map)

}