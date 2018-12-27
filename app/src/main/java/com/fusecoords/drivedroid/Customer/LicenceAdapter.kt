package com.fusecoords.drivedroid.Customer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.fusecoords.drivedroid.R

class LicenceAdapter(val items: ArrayList<Licence>, val context: Context) : RecyclerView.Adapter<LicenceHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LicenceHolder {
        return LicenceHolder(LayoutInflater.from(context).inflate(R.layout.item_licence, parent, false))
    }

    override fun onBindViewHolder(holder: LicenceHolder, position: Int) {
        holder.licenceNo.text = items.get(position).LicenceNo
    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }


}

class LicenceHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var licenceNo: TextView = view.findViewById(R.id.licenceNo)
}