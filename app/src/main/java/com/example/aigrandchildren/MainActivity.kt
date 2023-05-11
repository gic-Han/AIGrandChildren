package com.example.aigrandchildren

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onDeleteButtonClick(v: View) {
        val intent = Intent(this@MainActivity,FilelistActivity::class.java)
        startActivity(intent)
    }

    fun onCallButtonClick(v: View) {
        val intent = Intent(Intent.ACTION_DIAL)
        startActivity(intent)
    }
}