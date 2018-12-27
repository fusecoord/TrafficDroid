package com.fusecoords.drivedroid.Authority

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.fusecoords.drivedroid.R

class FineSelectAdapter(var items: ArrayList<Fine>, val context: Context) : RecyclerView.Adapter<FineSelectHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FineSelectHolder {
        return FineSelectHolder(LayoutInflater.from(context).inflate(R.layout.item_select_fine, parent, false))
    }

    override fun onBindViewHolder(holder: FineSelectHolder, position: Int) {
        holder.licenceNo.text = items.get(position).Offence
        holder.price.text = items.get(position).Penalty.toString()
        holder.check.isChecked = items.get(position).isChecked

        holder.check.setOnClickListener {
            if (holder.check.isChecked)
                items.get(position).isChecked = true
            else
                items.get(position).isChecked = false
            notifyItemChanged(position)
        }
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
    var check: CheckBox = view.findViewById(R.id.checkbox)
}