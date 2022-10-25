package com.gudeok.gudeokapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gudeok.gudeokapp.R
import com.gudeok.gudeokapp.activity.ContentActivity
import com.gudeok.gudeokapp.data.CommentData
import com.gudeok.gudeokapp.data.PostData

class CommentlistAdapter(private val context: Context, private val data: ArrayList<CommentData>): RecyclerView.Adapter<CommentlistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.commentlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val content: TextView = itemView.findViewById(R.id.commentContent)

        fun bind(item: CommentData) {
            content.text = "${item.author}: ${item.content}"
        }
    }
}