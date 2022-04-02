package com.gudeok.gudeokapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.gudeok.gudeokapp.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

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