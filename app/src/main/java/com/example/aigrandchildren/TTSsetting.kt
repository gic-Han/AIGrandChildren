package com.example.aigrandchildren

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class TTSsetting(private var response: String): AppCompatActivity(){
    private lateinit var tts: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this) {
            tts.language = Locale.KOREAN
            tts.setPitch(0.75f)
            tts.setSpeechRate(1.2f)
        }
        tts.speak(response, TextToSpeech.QUEUE_FLUSH,null,"id1")
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()

        super.onDestroy()
    }
}