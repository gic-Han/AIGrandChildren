package com.example.aigrandchildren

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

object TTSsetting{
    private lateinit var tts: TextToSpeech
    fun init(context: Context) {
        tts = TextToSpeech(context) {
            tts.language = Locale.KOREAN
            tts.setPitch(1.0f)
            tts.setSpeechRate(1.2f)
        }
    }

    fun speakUp(response: String){
        tts.speak(response, TextToSpeech.QUEUE_FLUSH,null,null)
    }

    fun stop() {
        tts.stop()
        tts.shutdown()
    }
}