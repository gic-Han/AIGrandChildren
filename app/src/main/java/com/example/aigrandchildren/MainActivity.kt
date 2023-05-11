package com.example.aigrandchildren

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onDeleteButtonClick() {
        val intent = Intent(this@MainActivity,FilelistActivity::class.java)
        startActivity(intent)
    }

    fun onCallButtonClick() {
        val intent = Intent(Intent.ACTION_DIAL)
        startActivity(intent)
    }
}