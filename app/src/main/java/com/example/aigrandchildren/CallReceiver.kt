package com.example.aigrandchildren

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.telephony.TelephonyManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {

    private var phoneNumber: String? = null
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            // 전화 상태가 변경되었을 때 실행됩니다.
            Log.d("call","실행: Action Phone State Changed")
            val phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (phoneState == TelephonyManager.EXTRA_STATE_RINGING) {
                // 전화가 왔을 때 실행됩니다.
                Log.d("call","전화번호 저장")
                phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                Log.d("call", "전화번호: $phoneNumber")
            }
        }
        if(intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            // SMS 수신 시 실행됩니다.
            Log.d("sms", "SMS 수신")
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>?
                if (pdus != null) {
                    for (pdu in pdus) {
                        val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                        phoneNumber = smsMessage.originatingAddress
                        val messageBody = smsMessage.messageBody

                        Log.d("sms", "전화번호: $phoneNumber")
                        Log.d("sms", "내용: $messageBody")
                    }
                }
            }
        }

        if (phoneNumber != null) {
            val intent1 = Intent(context, PhoneNumberSearch::class.java)
            intent1.putExtra("phoneNumber", phoneNumber)
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent1)
        }
    }
}