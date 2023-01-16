package com.fitdesgin

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment: BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottomsheet_fragment, container,false)
        val title = view.findViewById<TextView>(R.id.nama_lengkap)
        val address = view.findViewById<TextView>(R.id.address)
        val price = view.findViewById<TextView>(R.id.price)
        val experience = view.findViewById<TextView>(R.id.experience)
        val phoneNumberButton = view.findViewById<Button>(R.id.phoneNumber)

        val data = arguments
        title.text = data!!.get("title").toString()
        address.text = data!!.get("address").toString()
        price.text = data!!.get("price").toString()
        experience.text = data!!.get("experience").toString()
        Log.d(TAG, "https://wa.me/"+data!!.get("phoneNumber").toString())


        phoneNumberButton.setOnClickListener {
            val url = "https://wa.me/"+data!!.get("phoneNumber").toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            it.context.startActivity(intent)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}