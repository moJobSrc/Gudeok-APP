package com.gudeok.gudeokapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gudeok.gudeokapp.databinding.ActivityContentBinding
import com.gudeok.gudeokapp.networkModel.PostResponse
import com.gudeok.gudeokapp.retrofit.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class ContentActivity : AppCompatActivity() {

    lateinit var binding: ActivityContentBinding
    val retrofit = RetrofitManager.getClient()
    var post_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("post_id")) {
            post_id = intent.getIntExtra("post_id", -1)
            Log.d("id", post_id.toString())
        }

        binding.contentBackButton.setOnClickListener {
            finish()
        }

        retrofit.seePost(post_id).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                val response = response.body()
                if (response?.post != null) {
                    Log.d("response", response.post.toString())
                    binding.apply {
                        response.post.also { data ->
                            contentTitle.text = data.title
                            author.text = data.author
                            date.text = data.date
                            postContent.text = data.content
                            view.text = data.seen.toString()
                            beechu.text = data.beechu.toString()
                            gaechu.text = data.gaechu.toString()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
}