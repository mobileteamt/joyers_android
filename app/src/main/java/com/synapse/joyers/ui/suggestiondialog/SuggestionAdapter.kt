package com.synapse.joyers.ui.suggestiondialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.synapse.joyers.R

class SuggestionAdapter<T>(
    private val items: List<T>,
    private val parseItem: (T) -> String,
    private val onClick: (T) -> Unit
) : RecyclerView.Adapter<SuggestionAdapter<T>.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textSuggestion: AppCompatTextView = itemView.findViewById(R.id.username)
        val imageView: ImageView = itemView.findViewById(R.id.image_username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.suggestion_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textSuggestion.text = parseItem(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }
}
