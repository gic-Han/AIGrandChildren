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

class ChatbotActivity : AppCompatActivity() {

    companion object {
        val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
        private val MY_SECRET_KEY: String? = null // "Your Secret Key" 직접 넣어도 됨
    }

    private lateinit var recyclerview: RecyclerView
    private var ttssetting = TTSsetting
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private var messageList: MutableList<Message>? = null
    private var messageAdapter: MessageAdapter? = null
    private var client: OkHttpClient? = null
    private var lastchat: String? = null
    private var lastreply: String? = null
    private var isChat: Boolean = true
    set(value) {
        field = value
        if (value) {
//            recognizeVoiceInput()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("start", "now starting")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)
        val welcome: TextView = findViewById(R.id.tv_welcome)
        val chatting: EditText = findViewById(R.id.et_msg)
        val send: ImageButton = findViewById(R.id.btn_send)
        val recognize: ImageButton = findViewById(R.id.btn_voice)
        recyclerview = findViewById(R.id.recycler_view)
        recyclerview.setHasFixedSize(true)
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        recyclerview.layoutManager = manager
        messageList = ArrayList()
        messageAdapter = MessageAdapter(messageList as ArrayList<Message>)
        recyclerview.adapter = messageAdapter
        ttssetting.init(this)

//        if (MY_SECRET_KEY == null) {
//            fragment로 키 입력받기
//        }

        // 버튼을 눌러 채팅 입력
        send.setOnClickListener {
            val question = chatting.text.toString().trim { it <= ' ' }
            addToChat(question, Message.SENT_BY_ME)
            chatting.setText("")
            callAPI(question)
            isChat = false
            welcome.visibility = View.GONE
        }

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                    val resultData = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    chatting.setText(resultData!![0])
                }
            }

        recognize.setOnClickListener {
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
            recyclerview.smoothScrollToPosition(messageAdapter!!.itemCount)
        }
    }

    fun addResponse(response: String) {
//        messageList!!.removeAt(messageList!!.size - 1) '...' 안쓸거라 지움
        addToChat(response, Message.SENT_BY_BOT)
        isChat = ttssetting.speakUp(response)
        lastreply = response
    }

    private fun callAPI(question: String?) {
        //okhttp
        val arr = JSONArray()
        val baseAi = JSONObject()
        val userMsg = JSONObject()
        try {
            //AI 속성설정
            baseAi.put("role", "system")
            baseAi.put("content", "어르신을 위한 대답을 해줘. 50토큰 이내로.")
            if(lastreply != null) {
                baseAi.put("role", "user")
                baseAi.put("content", lastchat)
                baseAi.put("role", "assistant")
                baseAi.put("content", lastreply)
            }
            //유저 메세지
            userMsg.put("role", "user")
            userMsg.put("content", question)
            lastchat = question
            //array로 담아서 한번에 보낸다
            arr.put(baseAi)
            arr.put(userMsg)
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
//        messageList!!.add(Message("...", Message.SENT_BY_BOT)) 일단 지움
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
                addResponse("${e.message}로 인해 실패했습니다.")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonObject = JSONObject(response.body!!.string())
                    try {
                        val jsonArray = jsonObject.getJSONArray("choices")
                        val result =
                            jsonArray.getJSONObject(0).getJSONObject("message").getString("content")
                        addResponse(result.trim { it <= ' ' })
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    addResponse("${response.body!!.string()}으로 인해 실패했습니다.")
                }
            }
        })
    }

    override fun onDestroy() {
        ttssetting.stop()
        super.onDestroy()
    }
}
