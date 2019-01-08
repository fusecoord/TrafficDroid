package com.fusecoords.drivedroid.Authority

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fusecoords.drivedroid.R

class FineValidateAdapter(var items: ArrayList<Fine>, val context: Context) :
    RecyclerView.Adapter<FineValidateHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FineValidateHolder {

        return FineValidateHolder(LayoutInflater.from(context).inflate(R.layout.item_validate_find, parent, false))
    }

    override fun onBindViewHolder(holder: FineValidateHolder, position: Int) {
        holder.licenceNo.text = items.get(position).Offence



        if (items.get(position).violationList.size > 0) {
            holder.times.text = "(" + items.get(position).violationList.size.toString() + " times)"

            holder.times.visibility = View.VISIBLE
            var count =items.get(position).violationList.size+1
            var totalAmt = (items.get(position).Penalty * count)
            holder.price.text = "\u20B9" + totalAmt.toString()
        } else {
            holder.times.visibility = View.GONE
            holder.price.text = "\u20B9" + items.get(position).Penalty.toString()
        }
        var violationList = ""
        for (violation in items.get(position).violationList) {
            if (violationList.equals(""))
                violationList = violation.Date
            else
                violationList = violationList + "\n" + violation.Date
        }
        holder.expand.text = violationList
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.count()
    }

    fun filterList(filterdNames: ArrayList<Fine>) {
        items = filterdNames
        notifyDataSetChanged()
    }
}

class FineValidateHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var licenceNo: TextView = view.findViewById(R.id.licenceNo)
    var price: TextView = view.findViewById(R.id.price)
    var times: TextView = view.findViewById(R.id.times)
    var expand: TextView = view.findViewById(R.id.expand)
}