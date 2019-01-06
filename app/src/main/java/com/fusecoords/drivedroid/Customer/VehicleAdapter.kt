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
import com.fusecoords.drivedroid.Authority.Attachement
import com.fusecoords.drivedroid.Authority.UploadAttachement
import com.fusecoords.drivedroid.R

class VehicleAdapter(val items: ArrayList<Vehicle>, val context: Context) : RecyclerView.Adapter<VehicleHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleHolder {
        return VehicleHolder(LayoutInflater.from(context).inflate(R.layout.item_vehicle, parent, false))
    }

    override fun onBindViewHolder(holder: VehicleHolder, position: Int) {
        holder.vehicleNo.setText(items.get(position).VehicleNo)
        holder.regNo.setText(items.get(position).RegNo)
        holder.dateReg.setText(items.get(position).RegDate)
        holder.dateExpReg.setText(items.get(position).RcExpiry)
        holder.chasisNo.setText(items.get(position).ChasisNo)
        holder.engineNo.setText(items.get(position).EngineNo)
        holder.classV.setText(items.get(position).Class)
        holder.model.setText(items.get(position).Model)
        holder.fuelType.setText(items.get(position).FuelType)

        holder.insurance.tag = items.get(position)
        holder.reg.tag = items.get(position)
        holder.puc.tag = items.get(position)
        holder.taxation.tag = items.get(position)


        holder.taxation.setOnClickListener {

            goTo(2, (it.tag as Vehicle).VehicleNo!!)
        }
        holder.reg.setOnClickListener {
            goTo(1, (it.tag as Vehicle).VehicleNo!!)
        }
        holder.puc.setOnClickListener {
            goTo(3, (it.tag as Vehicle).VehicleNo!!)
        }
        holder.insurance.setOnClickListener {
            goTo(4, (it.tag as Vehicle).VehicleNo!!)
        }
    }

    fun goTo(flow: Int, vehicleNo: String) {

        Attachement.flow = flow
        Attachement.vehicleNo = vehicleNo
        context.startActivity(Intent(context, UploadAttachement::class.java))
    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class VehicleHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    var vehicleNo: EditText = view.findViewById(R.id.vehicleNo)
    var regNo: EditText = view.findViewById(R.id.regNo)
    var dateReg: EditText = view.findViewById(R.id.dateReg)
    var dateExpReg: EditText = view.findViewById(R.id.dateExpReg)
    var chasisNo: EditText = view.findViewById(R.id.chasisNo)
    var engineNo: EditText = view.findViewById(R.id.engineNo)
    var classV: EditText = view.findViewById(R.id.classV)
    var model: EditText = view.findViewById(R.id.model)
    var fuelType: EditText = view.findViewById(R.id.fuelType)


    var reg: Button = view.findViewById(R.id.reg)
    var taxation: Button = view.findViewById(R.id.taxation)
    var puc: Button = view.findViewById(R.id.puc)
    var insurance: Button = view.findViewById(R.id.insurance)
}