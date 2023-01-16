package com.fitdesgin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class RatingActivity : AppCompatActivity() {
    companion object{
        val EXTRA_NAME = "extra_name"
        val EXTRA_PHOTO = "extra_photo"
        val EXTRA_PHONE_NUMBER = "extra_phone_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val name: TextView = findViewById(R.id.name)
        val photo: ImageView = findViewById(R.id.photo)
        val btn_done: Button = findViewById(R.id.btn_done)

        val mySharedPreferences = getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)

        name.text = mySharedPreferences.getString(RatingActivity.EXTRA_NAME, "")
        Glide.with(this)
            .load(mySharedPreferences.getString(RatingActivity.EXTRA_PHOTO, ""))
            .apply(RequestOptions())
            .into(photo)

        // Push Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "My Notification", "my notificatioon",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_rounded_logo)

        val builder = NotificationCompat.Builder(this@RatingActivity, "My_Notification")

        builder.setContentTitle("Selamat, kamu mendapatkan 100 poin \uD83C\uDF89")
        builder.setContentText("Terima kasih sudah melakukan konsultasi di FitDesign")
        builder.setSmallIcon(R.drawable.ic_logo)
        builder.setLargeIcon(largeIcon)
        builder.setAutoCancel(true)
        //Set Channel untuk API26
        builder.setChannelId("My Notification")

        val managerCompat = NotificationManagerCompat.from(this@RatingActivity)

        btn_done.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            managerCompat.notify(0, builder.build())
            finish()
        }
    }
}