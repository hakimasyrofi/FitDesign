package com.fitdesgin

import android.content.ContentValues.TAG
import android.content.Intent
import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.fitdesgin.recview.ChooseDesignerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.android.gms.location.LocationRequest

class DashboardActivity : AppCompatActivity() {

    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val penjahit_terdekat = findViewById<ImageView>(R.id.penjahit_terdekat)
        val konsultasi_designer = findViewById<ImageView>(R.id.konsultasi_designer)
        val tv_name = findViewById<TextView>(R.id.tv_name)
        val nav_profile =  findViewById<LinearLayout>(R.id.nav_profile)
        val tv_poin : TextView = findViewById(R.id.tv_poin)

        nav_profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
            finish()
        }

        penjahit_terdekat.setOnClickListener {
            val intent_penjahit = Intent(this, MapsActivity::class.java)
            startActivity(intent_penjahit)
        }
        konsultasi_designer.setOnClickListener {
            val intent_designer = Intent(this, ChooseDesignerActivity::class.java)
            startActivity(intent_designer)
        }

        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        val imageList = ArrayList<SlideModel>()

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid

        //val documentReference: DocumentReference = db.collection("users").document(userId)

        db.collection("users").document(userId).addSnapshotListener { value, error ->
            value?.let {
                val name = it.data!!.get("name").toString()
                val poin = it.data!!.get("poin").toString()+" poin"
                tv_name.text = name
                tv_poin.text = poin
//                Log.e(TAG,"oooooooooooooooooooooooooo")
//                Log.e(TAG,name)
            }
        }

        db.collection("banner").get().addOnCompleteListener{ task->
            if (task.isSuccessful) {
                for (document in task.result!!){
                    imageList.add(SlideModel(document.getString("url")))
                    imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
//                    Log.d(TAG, "Hasil baca firebase")
//                    Log.d(TAG, document.getString("url").toString())
                }
            }

        }

    }

}