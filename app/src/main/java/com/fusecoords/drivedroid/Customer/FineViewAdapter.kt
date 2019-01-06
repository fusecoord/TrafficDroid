package com.fusecoords.drivedroid.Customer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fusecoords.drivedroid.Authority.Fine
import com.fusecoords.drivedroid.R

class FineViewAdapter(var items: ArrayList<Fine>, val context: Context) : RecyclerView.Adapter<FineSelectHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FineSelectHolder {
        return FineSelectHolder(LayoutInflater.from(context).inflate(R.layout.item_view_fine, parent, false))
    }

    override fun onBindViewHolder(holder: FineSelectHolder, position: Int) {
        holder.licenceNo.text = items.get(position).Offence
        holder.price.text = "\u20B9" + items.get(position).Penalty.toString()

        holder.section.text = "Section: " + items.get(position).Section.toString()

    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    fun filterList(filterdNames: ArrayList<Fine>) {
        items = filterdNames
        notifyDataSetChanged()
    }
}

class FineSelectHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var licenceNo: TextView = view.findViewById(R.id.licenceNo)
    var price: TextView = view.findViewById(R.id.price)
    var section: TextView = view.findViewById(R.id.section)

}