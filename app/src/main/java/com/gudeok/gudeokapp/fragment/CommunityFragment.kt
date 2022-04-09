package com.gudeok.gudeokapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gudeok.gudeokapp.R
import com.gudeok.gudeokapp.networkModel.PostListResponse
import com.gudeok.gudeokapp.retrofit.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityFragment : Fragment() {

    lateinit var postlistAdapater: PostlistAdapter
    private var postlistData: ArrayList<PostlistData> = ArrayList<PostlistData>()
    lateinit var bbslistView: RecyclerView
    private val retrofit = RetrofitManager.getClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community, container, false)

        bbslistView = view.findViewById<RecyclerView>(R.id.communityView)!!

        retrofit.loadbbsList(1).enqueue(object: Callback<PostListResponse> {
            override fun onResponse(call: Call<PostListResponse>, response: Response<PostListResponse>) {
                val response = response.body()?.postlist
                if (!response.isNullOrEmpty()) {
                    postlistData = response
//                    Log.d("bbslist", bbslistData.toString())
//                    bbslistData.add(bbslistData(title = "asdf",author = "me",
//                        date = Date(),id = 1,
//                        uuid = "",images = "",
//                        beechu = 0,gaechu = 1,
//                        comment = "",content = "asdfasdf",
//                        seen = 0))
                    postlistAdapater = PostlistAdapter(requireContext(), postlistData)

                    bbslistView.adapter = postlistAdapater
                }

            }

            override fun onFailure(call: Call<PostListResponse>, t: Throwable) {
                Log.e("bbslist", t.message.toString())
            }

        })

        return view
    }
}