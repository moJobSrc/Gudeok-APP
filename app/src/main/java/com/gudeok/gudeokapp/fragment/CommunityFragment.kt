package com.gudeok.gudeokapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gudeok.gudeokapp.R
import com.gudeok.gudeokapp.activity.WriteActivty
import com.gudeok.gudeokapp.adapter.PostlistAdapter
import com.gudeok.gudeokapp.data.PostData
import com.gudeok.gudeokapp.networkModel.PostListResponse
import com.gudeok.gudeokapp.retrofit.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommunityFragment : Fragment() {

    lateinit var postlistAdapater: PostlistAdapter
    private var postData: ArrayList<PostData> = ArrayList()
    lateinit var bbslistView: RecyclerView
    private val retrofit = RetrofitManager.retrofitClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community, container, false)

        bbslistView = view.findViewById(R.id.communityView)!!
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

        retrofit.loadbbsList(1).enqueue(object: Callback<PostListResponse> {
            override fun onResponse(call: Call<PostListResponse>, response: Response<PostListResponse>) {
                val response = response.body()?.postlist
                if (!response.isNullOrEmpty()) {
                    postData = response
                    postlistAdapater = PostlistAdapter(requireContext(), postData)

                    bbslistView.adapter = postlistAdapater
                }

            }

            override fun onFailure(call: Call<PostListResponse>, t: Throwable) {
                Log.e("bbslist", t.message.toString())
            }

        })

        fab.setOnClickListener {
            val intent = Intent(requireContext(), WriteActivty::class.java)
            startActivity(intent)
        }

        return view
    }
}