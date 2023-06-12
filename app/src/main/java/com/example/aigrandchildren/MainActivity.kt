package com.example.aigrandchildren

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_PHONE_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPhonePermission()
    }

    private fun checkPhonePermission() {
        // 필요한 권한을 배열로 정의합니다.
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,   // 전화 상태를 가져오기 위한 권한
            Manifest.permission.READ_CALL_LOG,      // 통화 로그를 읽기 위한 권한
            Manifest.permission.RECEIVE_SMS,        // SMS를 수신하기 위한 권한
            Manifest.permission.READ_SMS,           // SMS를 읽기 위한 권한
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )

        // 부여된 권한을 확인합니다.
        val grantedPermissions = permissions.filter {ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }

        if (grantedPermissions.size == permissions.size) {
            // 모든 권한이 부여
            Log.d("start", "시작")
        } else {
            // 필요한 권한이 부여되지 않은 경우, 사용자에게 권한 요청
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PHONE_PERMISSION)
        }
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
//        val packageName = "com.sec.android.app.camera"
//        val packageManager = packageManager
//        val intent = packageManager.getLaunchIntentForPackage(packageName)
//        if (intent != null) {
//            startActivity(intent)
//        }
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

    fun onKakaotalkButtonClick(v: View) {
        val packageName = "com.kakao.talk"
        val packageManager = packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent)
        }
    }

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