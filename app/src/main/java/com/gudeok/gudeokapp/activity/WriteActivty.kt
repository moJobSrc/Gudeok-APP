package com.gudeok.gudeokapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gudeok.gudeokapp.R
import com.gudeok.gudeokapp.databinding.ActivityLoginBinding
import com.gudeok.gudeokapp.databinding.ActivityWriteActivtyBinding
import com.gudeok.gudeokapp.networkModel.PostWriteResponse
import com.gudeok.gudeokapp.retrofit.RetrofitManager
import com.gudeok.gudeokapp.util.App
import com.gudeok.gudeokapp.util.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteActivty : AppCompatActivity() {
    private lateinit var binding: ActivityWriteActivtyBinding
    private val retrofit = RetrofitManager.retrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            write.setOnClickListener {
                retrofit.writePost(title.text.toString(), content.text.toString()).enqueue(object :
                    Callback<PostWriteResponse> {
                    override fun onResponse(
                        call: Call<PostWriteResponse>,
                        response: Response<PostWriteResponse>
                    ) {
                        if (response.isSuccessful) {
                            val writeResponse = response.body()
                            if (writeResponse != null) {
                                if (writeResponse.ok) {
                                    Toast.makeText(
                                        applicationContext,
                                        "글 작성 성공",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "글 작성 실패",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<PostWriteResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "글 작성 실패", Toast.LENGTH_SHORT).show()
                    }
                }
                )
            }
        }
    }
}