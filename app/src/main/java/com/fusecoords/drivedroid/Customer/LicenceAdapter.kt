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
        holder.dlNo.setText(items.get(position).LicenceNo)
        holder.doi.setText(items.get(position).Doi)
        holder.expdate.setText(items.get(position).Doe)
        holder.dld.setText(items.get(position).Dld)
        holder.auth.setText(items.get(position).Auth)
        var cov = ""
        var doi = ""
        if(items.get(position).licenceType!=null) {
            for (type in items.get(position).licenceType!!) {
                cov = type.LicenceType + "\n"
                doi = type.LicenceDOI + "\n"
            }
            holder.cov.visibility=View.VISIBLE
            holder.covdoi.visibility=View.VISIBLE
            holder.cov.setText(cov)
            holder.covdoi.setText(doi)
        }else{
            holder.cov.visibility=View.GONE
            holder.covdoi.visibility=View.GONE
        }
    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }


}

class LicenceHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var dlNo: EditText = view.findViewById(R.id.dlNo)
    var doi: EditText = view.findViewById(R.id.doi)
    var expdate: EditText = view.findViewById(R.id.expdate)
    var dld: EditText = view.findViewById(R.id.dld)
    var auth: EditText = view.findViewById(R.id.auth)
    var cov: EditText = view.findViewById(R.id.cov)
    var covdoi: EditText = view.findViewById(R.id.covdoi)
}