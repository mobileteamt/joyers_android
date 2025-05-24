package com.synapse.joyers.ui.signup.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.synapse.joyers.R
import com.synapse.joyers.apiData.response.Subtitle
import com.synapse.joyers.apiData.response.Title

class TitleNameAdapter(
    val context: Context,
    private val titleList: List<Title>?,
    private val subTitleList: List<Subtitle>?,
    private val onItemClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<TitleNameAdapter.TitleViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tvTitleName)
        val imgArrow: AppCompatImageView = itemView.findViewById(R.id.arrowText)

        init {
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onItemClick?.invoke(selectedPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.title_items, parent, false)
        return TitleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        if (titleList.isNullOrEmpty()) {
            holder.tvTitle.text = subTitleList?.get(position)?.name ?: ""
        } else {
            if (titleList[position].subtitles?.isNotEmpty()!!) {
                holder.imgArrow.visibility = View.VISIBLE
                holder.tvTitle.text = titleList[position].title
            }
            else {
                holder.imgArrow.visibility = View.GONE
                holder.tvTitle.text = titleList[position].title
            }

        }

        // Set text color based on selection
        if (position == selectedPosition) {
            holder.tvTitle.setTextColor(context.resources.getColor(R.color.golden)) // GOLDEN color
        } else {
            holder.tvTitle.setTextColor(context.resources.getColor(R.color.light_black)) // GOLDEN color
        }
    }

    override fun getItemCount(): Int =
        if (titleList.isNullOrEmpty()) subTitleList?.size ?: 0 else titleList.size
}
