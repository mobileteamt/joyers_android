package com.synapse.joyers.ui.ui.home.adapters

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.synapse.joyers.R
import com.synapse.joyers.ui.ui.home.MediaLayoutTwoFragment.MediaItem

class ImageAdapter(
    private var mediaItems: List<MediaItem>,
    private var overlayCount: Int = 0
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var perRowHeightPx: Int = 0
    private var rows: Int = 1

    // Default icon parameters
    private var closeIconMargin: Int = 10

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val overlayText: TextView = view.findViewById(R.id.overlayText)
        val playIcon: ImageView = view.findViewById(R.id.playIcon)
        val closeIcon: ImageView = view.findViewById(R.id.closeIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = mediaItems.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val mediaItem = mediaItems[position]

        when (mediaItem) {
            is MediaItem.Image -> {
                Glide.with(holder.itemView.context).load(mediaItem.url).into(holder.imageView)
                holder.playIcon.visibility = View.GONE
            }
            is MediaItem.Video -> {
                holder.playIcon.visibility = View.VISIBLE

            }
        }

        // Apply dynamic size/margin
        val closeIconLayoutParams = holder.closeIcon.layoutParams as ViewGroup.MarginLayoutParams

        Log.e("TAG", "onBindViewHolder: $closeIconMargin")
        closeIconLayoutParams.setMargins(closeIconMargin, closeIconMargin, closeIconMargin, closeIconMargin)
        holder.closeIcon.layoutParams = closeIconLayoutParams

        if (position == mediaItems.size - 1 && overlayCount > 0) {
            holder.overlayText.visibility = View.VISIBLE
            holder.overlayText.text = "+$overlayCount"
        } else {
            holder.overlayText.visibility = View.GONE
        }

        val itemHeight = perRowHeightPx
        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = itemHeight
        holder.itemView.layoutParams = layoutParams
    }

    fun updateMediaItems(
        mediaItems: List<MediaItem>,
        overlayCount: Int,
        perRowHeightPx: Int,
        rows: Int
    ) {
        this.mediaItems = mediaItems
        this.overlayCount = overlayCount
        this.perRowHeightPx = perRowHeightPx
        this.rows = rows
        notifyDataSetChanged()
    }

    // 🔄 Called by fragment/tab to adjust icon
    fun updatePlayIconParams(tabId: Int) {
        when (tabId) {
            100, 200 -> {
                closeIconMargin = 10
            }
            else -> {
                closeIconMargin = 15
            }
        }
        notifyDataSetChanged()
    }
}

// Helper function for dp to px conversion
fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

