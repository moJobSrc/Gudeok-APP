package com.gudeok.gudeokapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gudeok.gudeokapp.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: SharedPreferences
    private val retrofit = RetrofitManager.getClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences("gudeok", Context.MODE_PRIVATE)

        binding.apply {
            loginLoginBtn.setOnClickListener {
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
                            Log.d("Token", accessToken)
                            pref.edit().putString("accessToken", accessToken).apply()
                            val mainIntent = Intent(applicationContext, MainActivity::class.java);
                            mainIntent.putExtra("msg", "${loginId.text.toString()}님 반갑습니다")
                            startActivity(mainIntent)
                        } else {
                            Log.d("Token", "Token is null")
                        }

                    }

                })
            }

            loginRegisterBtn.setOnClickListener {
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