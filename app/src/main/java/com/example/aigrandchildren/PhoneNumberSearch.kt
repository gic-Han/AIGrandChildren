package com.example.aigrandchildren

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class PhoneNumberSearch: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.dialog)

        val OKbtn = findViewById<Button>(R.id.OK_button)

        val bundle = intent.extras
        val phoneNumber = bundle?.getString("phoneNumber")
        Log.d("call", "전화번호: $phoneNumber")

        if (searchPhoneNumber(phoneNumber!!)) {
            Log.d("dialog", "액티비티 OFF")
            finish()
        } else {
            Log.d("dialog", "액티비티 ON")
            OKbtn.setOnClickListener {
                finish()
            }
        }
    }

    private fun searchPhoneNumber(Number: String): Boolean {
        val phoneNumber = convertPhoneNumber(Number)
        Log.d("dialog", "번호 형식 변경 $phoneNumber")

        val contactscontractUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val selection = "${ContactsContract.CommonDataKinds.Phone.NUMBER} = ?"
        val selectionArgs = arrayOf(phoneNumber)
        Log.d("dialog", "번호 찾는 중 ${selectionArgs[0]}")
        contentResolver.query(contactscontractUri, projection, selection, selectionArgs, null)?.use { cursor ->
            if (cursor.count > 0) {
                Log.d("dialog", "번호 있음 ${cursor.count}")
                cursor.moveToFirst()
                Log.d("dialog", cursor.getString(0))
                return true
            }
        }

        Log.d("dialog", "번호 없음")
        return false
    }
    private fun convertPhoneNumber(phone: String) : String{
        return if(phone.startsWith("02")){
            if(phone.length < 10){
                phone.substring(0, 2) + "-" + phone.substring(2, 5) + "-" + phone.substring(5)
            } else {
                phone.substring(0, 2) + "-" + phone.substring(2, 6) + "-" + phone.substring(6)
            }
        } else {
            if(phone.length < 11){
                phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6)
            } else {
                phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7)
            }
        }
    }
}