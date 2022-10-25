package com.gudeok.gudeokapp.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gudeok.gudeokapp.R
import com.gudeok.gudeokapp.adapter.CommentlistAdapter
import com.gudeok.gudeokapp.adapter.PostlistAdapter
import com.gudeok.gudeokapp.data.CommentData
import com.gudeok.gudeokapp.databinding.ActivityContentBinding
import com.gudeok.gudeokapp.networkModel.CommentListResponse
import com.gudeok.gudeokapp.networkModel.CommentWriteResponse
import com.gudeok.gudeokapp.networkModel.PostResponse
import com.gudeok.gudeokapp.retrofit.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContentActivity : AppCompatActivity() {

    lateinit var commentlistAdapter: CommentlistAdapter
    lateinit var binding: ActivityContentBinding
    lateinit var commentList: RecyclerView
    val retrofit = RetrofitManager.retrofitClient
    var post_id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        commentList = findViewById(R.id.commentList)

        if (intent.hasExtra("post_id")) {
            post_id = intent.getIntExtra("post_id", -1)
            Log.d("id", post_id.toString())
        }
        binding.apply {
            contentBackButton.setOnClickListener {
                finish()
            }

            commentWriteButton.setOnClickListener {
                // 댓글 작성
                retrofit.writeComment(post_id, commentWriteEditText.text.toString(), null).enqueue(object : Callback<CommentWriteResponse> {
                    override fun onResponse(
                        call: Call<CommentWriteResponse>,
                        response: Response<CommentWriteResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("writeComment", "success")
                            commentWriteEditText.text?.clear()
                        } else {
                            Log.d("writeComment", "fail")
                        }
                    }

                    override fun onFailure(call: Call<CommentWriteResponse>, t: Throwable) {
                        Log.d("writeComment", "fail")
                    }
                })
            }
        }

        retrofit.loadCommentList(post_id).enqueue(object : Callback<CommentListResponse> {
            override fun onResponse(call: Call<CommentListResponse>, response: Response<CommentListResponse>) {
                val response = response.body()?.commentList
                if (!response.isNullOrEmpty()) {
                    commentlistAdapter = CommentlistAdapter(applicationContext, response)
                    commentList.layoutManager = LinearLayoutManager(applicationContext)
                    commentList.adapter = commentlistAdapter
                    commentlistAdapter.notifyDataSetChanged()
                    Log.d("loadCommentList", "success")
                }
            }

            override fun onFailure(call: Call<CommentListResponse>, t: Throwable) {
                Log.d("loadCommentList", t.message!!)
            }
        })

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
                Log.d("seePost", t.message!!)
            }

        })

    }
}