package com.fusecoords.drivedroid.Authority

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fusecoords.drivedroid.R

class HistoryAdapter(var items: ArrayList<Violation>, val context: Context) :
    RecyclerView.Adapter<HistoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {

        return HistoryHolder(LayoutInflater.from(context).inflate(R.layout.item_history, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.licenceNo.text = items.get(position).Offence
        holder.price.text = "\u20B9" + items.get(position).TotalAmount.toString()
        holder.expand.text = items.get(position).Date
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.count()
    }

    fun filterList(filterdNames: ArrayList<Violation>) {
        items = filterdNames
        notifyDataSetChanged()
    }
}

class HistoryHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var licenceNo: TextView = view.findViewById(R.id.licenceNo)
    var price: TextView = view.findViewById(R.id.price)
    var times: TextView = view.findViewById(R.id.times)
    var expand: TextView = view.findViewById(R.id.expand)
}