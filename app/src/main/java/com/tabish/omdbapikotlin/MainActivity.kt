package com.tabish.omdbapikotlin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import com.tabish.omdbapikotlin.R

class MainActivity : AppCompatActivity() {

    lateinit var developedBy : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        developedBy = findViewById(R.id.DevelopedBy)

        developedBy.setText("Developed by Tabish Shamim.")
        developedBy.setTextColor(Color.parseColor("#000000"))

        developedBy.animate().alpha(1f).setDuration(5000)

        var timer = object: CountDownTimer(7000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }
            override fun onFinish() {

                var i : Intent = Intent(applicationContext,MovieList::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
            }
        }
        timer.start()
    }
}