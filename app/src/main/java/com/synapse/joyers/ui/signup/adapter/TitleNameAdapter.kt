package com.synapse.joyers.ui.signup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synapse.joyers.R

class TitleNameAdapter(private val titleList: List<String>) :
    RecyclerView.Adapter<TitleNameAdapter.TitleViewHolder>() {

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitleName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.title_items, parent, false)
        return TitleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        holder.tvTitle.text = titleList[position]
    }

    override fun getItemCount(): Int = titleList.size
}



