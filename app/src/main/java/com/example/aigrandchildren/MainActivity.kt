package com.example.aigrandchildren

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun onChatButtonClick(v: View) {
        val intent = Intent(this@MainActivity,ChatbotActivity::class.java)
        startActivity(intent)
    }

    fun onDeleteButtonClick(v: View) {
        val intent = Intent(this@MainActivity,FilelistActivity::class.java)
        intent.putExtra("deleteApp", true)
        startActivity(intent)
    }

    fun onFileButtonClick(v: View) {
        val intent = Intent(this@MainActivity,FilelistActivity::class.java)
        intent.putExtra("deleteApp", false)
        startActivity(intent)
    }

    fun onCallButtonClick(v: View) {
        val intent = Intent(Intent.ACTION_DIAL)
        startActivity(intent)
    }

    fun onCameraButtonClick(v: View) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(intent)
    }

    fun onYoutubeButtonClick(v: View) {
        val packageName = "com.google.android.youtube"
        val packageManager = packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent)
        }
    }
    // 암시적 인텐트 찾긴 했는데 카메라랑 유튜브는 못찾음 갤러리는 암시적 인텐트 있음
    fun onInternetButtonClick(v: View) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_BROWSER)
//        val packageName = "com.android.chrome"
//        val packageManager = packageManager
//        val intent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(intent)
    }

    fun onMessageButtonClick(v: View) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_MESSAGING)
//        val packageName = "com.samsung.android.messaging"
//        val packageManager = packageManager
//        val intent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(intent)
    }
}