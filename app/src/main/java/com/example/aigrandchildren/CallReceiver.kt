package com.example.aigrandchildren

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            // 전화 상태가 변경되었을 때 실행됩니다.
            Log.d("call","실행: Action Phone State Changed")
            val phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (phoneState == TelephonyManager.EXTRA_STATE_RINGING) {
                // 전화가 왔을 때 실행됩니다.
                Log.d("call","전화번호 저장")
                val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                Log.d("call", "전화번호: $phoneNumber")
            }
        }
    }
}