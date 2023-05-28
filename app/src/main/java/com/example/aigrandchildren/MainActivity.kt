package com.example.aigrandchildren

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val REQUEST_PHONE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPhonePermission()
    }

    private fun checkPhonePermission() {
        // 필요한 권한을 배열로 정의합니다.
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,  // 전화 상태를 가져오기 위한 권한
            Manifest.permission.READ_CALL_LOG,     // 통화 로그를 읽기 위한 권한
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )

        // 부여된 권한을 확인합니다.
        val grantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (grantedPermissions.size == permissions.size) {
            // 모든 권한이 이미 부여되어 있으므로 전화번호를 가져옵니다.
            Log.d("call", "전화번호 가져오기")
        } else {
            // 필요한 권한이 부여되지 않은 경우, 사용자에게 권한 요청을 합니다.
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PHONE_PERMISSION)
        }
    }

    fun onChatButtonClick() {
        val intent = Intent(this@MainActivity,ChatbotActivity::class.java)
        startActivity(intent)
    }

    fun onDeleteButtonClick() {
        val intent = Intent(this@MainActivity,FilelistActivity::class.java)
        intent.putExtra("deleteApp", true)
        startActivity(intent)
    }

    fun onFileButtonClick() {
        val intent = Intent(this@MainActivity,FilelistActivity::class.java)
        intent.putExtra("deleteApp", false)
        startActivity(intent)
    }

    fun onCallButtonClick() {
        val intent = Intent(Intent.ACTION_DIAL)
        startActivity(intent)
    }

    fun onCameraButtonClick() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val packageName = "com.sec.android.app.camera"
//        val packageManager = packageManager
//        val intent = packageManager.getLaunchIntentForPackage(packageName)
//        if (intent != null) {
//            startActivity(intent)
//        }
        startActivity(intent)
    }

    fun onYoutubeButtonClick() {
        val packageName = "com.google.android.youtube"
        val packageManager = packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent)
        }
    }

    fun onInternetButtonClick() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_BROWSER)
//        val packageName = "com.android.chrome"
//        val packageManager = packageManager
//        val intent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(intent)
    }

    fun onMessageButtonClick() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_MESSAGING)
//        val packageName = "com.samsung.android.messaging"
//        val packageManager = packageManager
//        val intent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(intent)
    }
}