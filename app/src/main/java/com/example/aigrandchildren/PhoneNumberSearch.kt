package com.example.aigrandchildren

import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.aigrandchildren.dialog.CustomDialog

class PhoneNumberSearch: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        val phoneNumber = bundle?.getString("phoneNumber")
        Log.d("call", "전화번호: $phoneNumber")

        if (searchPhoneNumber(phoneNumber)) {
            // 있음
        } else {
            val fm = supportFragmentManager
            val dialogFragment = CustomDialog()
            dialogFragment.show(fm, "CustomDialog")
            Log.d("dialog", "팝업 띄움")
        }
    }

    private fun searchPhoneNumber(phoneNumber: String?): Boolean {
        val callLogUri = CallLog.Calls.CONTENT_URI
        val projection = arrayOf(CallLog.Calls.NUMBER)
        val selection = "${CallLog.Calls.NUMBER} = ?"
        val selectionArgs = arrayOf(phoneNumber)

        Log.d("dialog", "번호 찾는 중 ${selectionArgs[0]}")
        contentResolver.query(callLogUri, projection, selection, selectionArgs, null)?.use { cursor ->
            if (cursor.count > 0) {
                Log.d("dialog", "번호 있음 ${cursor.count}")
                return true
            }
        }

        Log.d("dialog", "번호 없음")
        return false
    }
}