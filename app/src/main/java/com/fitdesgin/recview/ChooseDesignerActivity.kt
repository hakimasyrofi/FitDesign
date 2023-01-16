package com.fitdesgin.recview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import java.util.*
import com.fitdesgin.R
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChooseDesignerActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var designerArrayList : ArrayList<Designer>
    private lateinit var designerAdapter: DesignerAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_designer)

        recyclerView = findViewById(R.id.rv_designer)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.setHasFixedSize(true)

        designerArrayList = ArrayList()

        designerAdapter = DesignerAdapter(designerArrayList)
        recyclerView.adapter = designerAdapter

        EventChangeListener()
    }

    private fun EventChangeListener(){
        db = Firebase.firestore
        db.collection("designers")
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            designerArrayList.add(dc.document.toObject(Designer::class.java))
                        }
                    }
                    designerAdapter.notifyDataSetChanged()
                }
            })

    }
}