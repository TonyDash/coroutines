package com.tiga.coroutines

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLayout()
    }

    private fun initLayout() {
        btnPractice1.setOnClickListener {
            startActivity(Intent(this@MainActivity,PracticeActivity1::class.java))
        }
        btnPractice2.setOnClickListener {
            startActivity(Intent(this@MainActivity,PracticeActivity2::class.java))
        }
    }
}