package com.fitdesgin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView

class CountDownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)

        message()
    }

    fun message(){
        object : CountDownTimer(10000, 1000) {
            val countdown: TextView = findViewById(R.id.countdown)
            override fun onTick(millisUntilFinished: Long) {
                countdown.visibility = View.VISIBLE
                countdown.text = ((millisUntilFinished/1000)+10).toString()+" detik"
            }
            override fun onFinish() {
                countdown.visibility = View.INVISIBLE
                countdown.text = ""
                val intent = Intent()
                intent.setClassName("com.fitdesgin", "com.fitdesgin.ChatActivity")
                startActivityForResult(intent, 0)
            }
        }.start()
    }
}