package com.gudeok.gudeokapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gudeok.gudeokapp.networkModel.TokenCheckResponse
import com.gudeok.gudeokapp.retrofit.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {
    private val retrofit = RetrofitManager.getClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            isLoginAlive()
        }, 300);

        isLoginAlive()
    }

    private fun isLoginAlive() {
        //token 만료 확인 (aka 자동로그인)
        retrofit.tokenCheckRequest().enqueue(object: Callback<TokenCheckResponse> {
            override fun onResponse(call: Call<TokenCheckResponse>, response: Response<TokenCheckResponse>) {
                val resbody = response.body()
                if (resbody != null) {
                    val isOk = resbody.ok

                    Log.d("Token isn't Expired", isOk.toString())
                    if (isOk) {
                        val mainIntent = Intent(applicationContext, MainActivity::class.java);
                        startActivity(mainIntent)
                        finish()
                    } else {
                        val loginIntent = Intent(applicationContext, LoginActivity::class.java);
                        startActivity(loginIntent)
                        finish()
                    }
                } else {
                    val loginIntent = Intent(applicationContext, LoginActivity::class.java);
                    startActivity(loginIntent)
                    finish()
                }
            }

            override fun onFailure(call: Call<TokenCheckResponse>, t: Throwable) {
                Log.e("retrofit", t.toString())
            }

        })
    }
}