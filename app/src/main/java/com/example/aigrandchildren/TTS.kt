package com.example.aigrandchildren

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class TTS : Activity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tts = TextToSpeech(this, this)
    }

    private fun speakOut(InputText: CharSequence) {
        val text: CharSequence = InputText
        tts!!.setPitch(0.75.toFloat())
        tts!!.setSpeechRate(1.2.toFloat())
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1")
    }

    public override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.KOREA)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported")
            } else {
                speakOut("")
            }
        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }
}