package com.fitdesgin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ChatActivity : AppCompatActivity() {
    companion object{
        val EXTRA_NAME = "extra_name"
        val EXTRA_PHOTO = "extra_photo"
        val EXTRA_PHONE_NUMBER = "extra_phone_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid

        val name: TextView = findViewById(R.id.nama_lengkap)
        val photo: ImageView = findViewById(R.id.photo_chat)
        val welcome: TextView = findViewById(R.id.welcome)
        val tv_end: TextView = findViewById(R.id.tv_end)
        val iv_send: ImageView = findViewById(R.id.iv_send)
        val et_chat: EditText = findViewById(R.id.et_chat)
        val frg_lyt: FrameLayout = findViewById(R.id.frameLayout)

        val welcome_user: TextView = findViewById(R.id.welcome_user)
        val frg_lyt1: FrameLayout = findViewById(R.id.frameLayout1)

        val welcome_user2: TextView = findViewById(R.id.welcome_user2)
        val frg_lyt2: FrameLayout = findViewById(R.id.frameLayout2)

        val welcome_user3: TextView = findViewById(R.id.welcome_user3)
        val frg_lyt3: FrameLayout = findViewById(R.id.frameLayout3)

        val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)

        Handler().postDelayed(
                Runnable (){
                    run(){
                        frg_lyt.alpha = 1.0f
                    }
                },1000)
        var count = 1

        iv_send.setOnClickListener {
            if (count == 1){
                welcome_user.text = et_chat.text.toString()
                frg_lyt1.alpha = 1.0f
            }
            else if (count == 2){
                welcome_user2.text = et_chat.text.toString()
                frg_lyt2.alpha = 1.0f

                Handler().postDelayed(
                        Runnable (){
                            run(){
                                frg_lyt3.alpha = 1.0f
                            }
                        },2000)
            }
            et_chat.setText("")
            count += 1
        }

        name.text = mySharedPreferences.getString(EXTRA_NAME, "")
        Glide.with(this)
                .load(mySharedPreferences.getString(EXTRA_PHOTO, ""))
                .apply(RequestOptions())
                .into(photo)
        welcome.text = "Selamat sore, perkenalkan saya "+ name.text +" desainer FitDesign, ada yang bisa saya bantu?"

        var poin = 0
        db.collection("users").document(userId).addSnapshotListener { value, error ->
            value?.let {
                poin = Integer.parseInt(it.data!!.get("poin").toString())
            }
        }
        tv_end.setOnClickListener {
            db.collection("users").document(userId).update("poin", poin+100)
            startActivity(Intent(this,RatingActivity::class.java))
            finish()
        }
    }
}