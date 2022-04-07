package com.gudeok.gudeokapp.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gudeok.gudeokapp.R

class bbslistAdapter(private val context: Context,private val data: ArrayList<bbslistData>): RecyclerView.Adapter<bbslistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bbslist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val title: TextView = itemView.findViewById(R.id.bbslistTitle)
        private val author: TextView = itemView.findViewById(R.id.bbslistAuthor)
        private val summary: TextView = itemView.findViewById(R.id.bbslistSummary)
        private val date: TextView = itemView.findViewById(R.id.bbslistDate)
        private val seen: TextView = itemView.findViewById(R.id.bbslistSeen)
        private val gaechu: TextView = itemView.findViewById(R.id.bbslistGaechu)
        private val comment: TextView = itemView.findViewById(R.id.bbslistComment)

        fun bind(item: bbslistData) {
            title.text = item.title
            author.text = item.author
            summary.text = item.content
            date.text = item.date.toString()
            comment.text = "0"
            gaechu.text = item.gaechu.toString()
            seen.text = item.seen.toString()
        }

    }
}