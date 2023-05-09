package com.example.aigrandchildren

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aigrandchildren.adapter.MessageAdapter
import com.example.aigrandchildren.model.Message
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var recycler_view: RecyclerView
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private var messageList: MutableList<Message>? = null
    private var messageAdapter: MessageAdapter? = null
    private var client: OkHttpClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("start","now starting")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv_welcome: TextView = findViewById(R.id.tv_welcome)
        val et_msg: EditText = findViewById(R.id.et_msg)
        val btn_send: ImageButton = findViewById(R.id.btn_send)
        val btn_voice: ImageButton = findViewById(R.id.btn_voice)
        recycler_view = findViewById(R.id.recycler_view)
        recycler_view.setHasFixedSize(true)
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        recycler_view.layoutManager = manager
        messageList = ArrayList()
        messageAdapter = MessageAdapter(messageList as ArrayList<Message>)
        recycler_view.adapter = messageAdapter

        Log.d("start","now starting")

        // 버튼을 눌러 채팅 입력
        btn_send.setOnClickListener {
            val question = et_msg.text.toString().trim { it <= ' ' }
            addToChat(question, Message.SENT_BY_ME)
            et_msg.setText("")
            callAPI(question)
            tv_welcome.visibility = View.GONE
        }

        // startActivityForResult가 Deprecated되서 registerForActivityResult를 사용
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val resultData = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                et_msg.setText(resultData!![0])
            }
        }
        /*
        버튼을 눌러 음성인식
        버튼 안누르고 대화하는 방법으로 하려면 말하는 상태, 답하는 상태를 나눠서 주고 받는 식으로 하면 될듯
        var talkingState = true
        if(talkingState){
            // 음성인식하는 함수
        }
         */
        btn_voice.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko_KR")
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성인식을 시작합니다.")
            activityResultLauncher!!.launch(intent)
        }
        client = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addToChat(message: String?, sentBy: String?) {
        runOnUiThread {
            messageList!!.add(Message(message, sentBy))
            messageAdapter!!.notifyDataSetChanged()
            recycler_view.smoothScrollToPosition(messageAdapter!!.itemCount)
        }
    }

    fun addResponse(response: String?) {
        messageList!!.removeAt(messageList!!.size - 1)
        addToChat(response, Message.SENT_BY_BOT)
    }
// 할일 - TTS 넣기, 이전 대화 저장 및 적용
    private fun callAPI(question: String?) {
        //okhttp
        messageList!!.add(Message("...", Message.SENT_BY_BOT))
        val arr = JSONArray()
        val baseAi = JSONObject()
        val userMsg = JSONObject()
        try {
            //AI 속성설정
            baseAi.put("role", "user")
            baseAi.put("content", "You are a helpful and kind AI Assistant.")
            //유저 메세지
            userMsg.put("role", "user")
            userMsg.put("content", question)
            //array로 담아서 한번에 보낸다
            arr.put(baseAi)
            arr.put(userMsg)
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
        val `object` = JSONObject()
        try {
            `object`.put("model", "gpt-3.5-turbo")
            `object`.put("messages", arr)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body: RequestBody = `object`.toString().toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer $MY_SECRET_KEY")
            .post(body)
            .build()
        client!!.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                addResponse("Failed to load response due to " + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonObject = JSONObject(response.body!!.string())
                    try {
                        val jsonArray = jsonObject.getJSONArray("choices")
                        val result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content")
                        addResponse(result.trim { it <= ' ' })
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body!!.string())
                }
            }
        })
    }

    companion object {
        val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
        private const val MY_SECRET_KEY = "{Your Secret Key}"
    }
}