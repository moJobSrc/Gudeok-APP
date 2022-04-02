package com.gudeok.gudeokapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gudeok.gudeokapp.networkModel.LogInCheckOkResponse
import com.gudeok.gudeokapp.networkModel.ResponseDTO
import com.gudeok.gudeokapp.databinding.ActivityLoginBinding
import com.gudeok.gudeokapp.networkModel.TokenCheckResponse
import com.gudeok.gudeokapp.util.App
import com.gudeok.gudeokapp.util.Prefs
import com.gudeok.gudeokapp.retrofit.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val pref: Prefs = App.prefs
    private val retrofit = RetrofitManager.getClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            loginLoginBtn.setOnClickListener {
                //로그인 리퀘스트 전송
                retrofit.loginRequest(loginId.text.toString(), loginPw.text.toString()).enqueue(object: Callback<LogInCheckOkResponse> {
                    override fun onFailure(call: retrofit2.Call<LogInCheckOkResponse>, t: Throwable) {
                        Log.e("retrofit", t.toString())
                    }

                    override fun onResponse(
                        call: retrofit2.Call<LogInCheckOkResponse>,
                        response: retrofit2.Response<LogInCheckOkResponse>
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
                //회원가입 리퀘스트 전송
                retrofit.registerRequest(loginId.text.toString(), loginPw.text.toString()).enqueue(object: Callback<ResponseDTO> {
                    override fun onResponse(
                        call: Call<ResponseDTO>,
                        response: Response<ResponseDTO>
                    ) {
                        Log.d("retrofit", response.body().toString())
                    }

                    override fun onFailure(call: Call<ResponseDTO>, t: Throwable) {
                        Log.e("retrofit", t.toString())
                    }
                })
            }
        }

    }
}