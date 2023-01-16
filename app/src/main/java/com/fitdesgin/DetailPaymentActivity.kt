package com.fitdesgin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fitdesgin.recview.ChooseDesignerActivity

class DetailPaymentActivity : AppCompatActivity() {
    companion object{
        val EXTRA_NAME = "extra_name"
        val EXTRA_PHOTO = "extra_photo"
        val EXTRA_PHONE_NUMBER = "extra_phone_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_payment)

        val name: TextView = findViewById(R.id.nama_lengkap)
        val photo: ImageView = findViewById(R.id.photo_chat)
        val btn_confirm: Button = findViewById(R.id.btn_confirm)
        val btn_back: ImageView = findViewById(R.id.btn_back)

        name.text =intent.getStringExtra(EXTRA_NAME)
        Glide.with(this)
                .load(intent.getStringExtra(EXTRA_PHOTO))
                .apply(RequestOptions())
                .into(photo)

        btn_confirm.setOnClickListener{
            val intent = Intent(this, PaymentSuccessActivity::class.java)
            startActivity(intent)
        }

        btn_back.setOnClickListener {
            val intent = Intent(this, ChooseDesignerActivity::class.java)
            startActivity(intent)
        }
    }
}