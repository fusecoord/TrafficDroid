package com.fusecoords.drivedroid.Customer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fusecoords.drivedroid.Authority.Alert

import com.fusecoords.drivedroid.R
import kotlinx.android.synthetic.main.activity_send_alert.view.*
import kotlinx.android.synthetic.main.fragment_alert.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlertFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AlertFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        var Alert: Alert? = null;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_alert, container, false)
        view.address.text = Alert!!.Address
        view.alert.text = Alert!!.Note
        view.date.text = Alert!!.DateTime
        return view
    }


}
