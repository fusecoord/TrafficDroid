package com.fusecoords.drivedroid.Customer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.fusecoords.drivedroid.R

class LicenceTypeAdapter(val items: ArrayList<LicenceType>, val context: Context) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_licence_type, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.issueDate.setText(items.get(position).LicenceDOI)
        holder.type.setText(items.get(position).LicenceType)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var issueDate: TextView = view.findViewById(R.id.issueDate)
    var type: TextView = view.findViewById(R.id.type)
}