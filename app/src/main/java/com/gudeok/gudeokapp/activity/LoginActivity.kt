package com.gudeok.gudeokapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gudeok.gudeokapp.databinding.ActivityLoginBinding
import com.gudeok.gudeokapp.networkModel.LogInCheckOkResponse
import com.gudeok.gudeokapp.networkModel.ResponseDTO
import com.gudeok.gudeokapp.retrofit.RetrofitManager
import com.gudeok.gudeokapp.util.App
import com.gudeok.gudeokapp.util.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val pref: Prefs = App.prefs
    private val retrofit = RetrofitManager.retrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            loginLoginBtn.setOnClickListener {
                //로그인 리퀘스트 전송
                retrofit.loginRequest(loginId.text.toString(), loginPw.text.toString()).enqueue(object: Callback<LogInCheckOkResponse> {
                    override fun onFailure(call: Call<LogInCheckOkResponse>, t: Throwable) {
                        Log.e("retrofit", t.toString())
                    }

                    override fun onResponse(
                        call: Call<LogInCheckOkResponse>,
                        response: Response<LogInCheckOkResponse>
                    ) {
                        val accessToken = response.body()?.accessToken.toString()
                        if (accessToken != "null") {
                            //로그인 성공
                            pref.token = accessToken
                            Log.d("Token", App.prefs.token?: "")
                            val mainIntent = Intent(applicationContext, MainActivity::class.java);
//                            mainIntent.putExtra("msg", "${loginId.text.toString()}님 반갑습니다")
                            startActivity(mainIntent)
                            finish()
                        } else {
                            Log.d("Token", "Token is null")
                        }

                    }

                })
            }

            loginRegisterBtn.setOnClickListener {
                //textview에 입력된 값이 비어있는지 확인
                if (loginId.text.toString() == "" || loginPw.text.toString() == "") {
                    Toast.makeText(applicationContext, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    //회원가입 리퀘스트 전송
                    retrofit.registerRequest(loginId.text.toString(), loginPw.text.toString()).enqueue(object: Callback<ResponseDTO> {
                        override fun onResponse(
                            call: Call<ResponseDTO>,
                            response: Response<ResponseDTO>
                        ) {
                            Log.d("retrofit", response.body().toString())
                            Toast.makeText(applicationContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {
                            Log.e("retrofit", t.message.toString())
                        }
                    })
                }
            }
        }

    }
}