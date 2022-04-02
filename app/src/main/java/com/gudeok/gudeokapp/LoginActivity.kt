package com.gudeok.gudeokapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login_register_btn: TextView = findViewById(R.id.login_register_btn)

        login_register_btn.setOnClickListener {
            val register_url = "http://39.113.240.156:3000/login"
            val client = OkHttpClient()
            val formBody = FormBody.Builder()
                .add("id","test")
                .add("pw","test")
                .build()

            val request = Request.Builder()
                .url(register_url)
                .post(formBody)
                .build()


            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("에러에러","실패시 메세지")
                    Log.d("에러에러", e.printStackTrace().toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    println("성공 메세지")
                    println(response.body?.string())
                    println(response)
                }

            })
            println("asdf--------------------")
        }

    }
}