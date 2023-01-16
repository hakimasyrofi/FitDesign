package com.fitdesgin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val log_out = findViewById<Button>(R.id.log_out)
        val nav_home = findViewById<LinearLayout>(R.id.nav_home)
        val tv_name = findViewById<TextView>(R.id.name_profile)
        val tv_email = findViewById<TextView>(R.id.email_profile)

        val firebaseAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val userId = firebaseAuth.currentUser!!.uid

        db.collection("users").document(userId).addSnapshotListener { value, error ->
            value?.let {
                val name = it.data!!.get("name").toString()
                tv_name.text = name
//                Log.e(TAG,"oooooooooooooooooooooooooo")
//                Log.e(TAG,name)
            }
        }

        tv_email.text = firebaseAuth.currentUser!!.email

        nav_home.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
            finish()
        }

        log_out.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            firebaseAuth.signOut()
        }
    }
}