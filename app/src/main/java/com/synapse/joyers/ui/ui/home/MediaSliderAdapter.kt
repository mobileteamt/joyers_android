package com.synapse.joyers.ui.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synapse.joyers.R

class MediaPagerAdapter(
    private val items: List<Int>,
    private val selectedTabValue: Int,
    private val density: Float
) : RecyclerView.Adapter<MediaPagerAdapter.MediaViewHolder>() {

    inner class MediaViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.mediaImage)
        val close: ImageView = view.findViewById(R.id.closeIcon)
        val play: ImageView = view.findViewById(R.id.playIcon)
        val moreOverlay: TextView = view.findViewById(R.id.moreOverlay)
        val count: TextView = view.findViewById(R.id.imageCounter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_grid_item, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val itemRes = items[position]
        holder.img.setImageResource(itemRes)
        holder.img.scaleType = ImageView.ScaleType.CENTER_CROP
        holder.img.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        holder.play.visibility = if (position == 2) View.VISIBLE else View.GONE
        holder.close.visibility = if (position == 5) View.GONE else View.VISIBLE
        holder.moreOverlay.visibility = View.GONE

        holder.count.visibility = View.VISIBLE
        holder.count.text = "${position + 1} / ${items.size}"

        // Dynamic margin and size values
        val marginDp = if (selectedTabValue == 100 || selectedTabValue == 200) 10 else 15
        val iconSizeDp = if (selectedTabValue == 100 || selectedTabValue == 200) 22 else 25
        val countSizeSp = if (selectedTabValue == 100 || selectedTabValue == 200) 10f else 12f

        val marginPx = (marginDp * density).toInt()
        val iconSizePx = (iconSizeDp * density).toInt()

        // Apply margin and size to close icon
        val closeParams = holder.close.layoutParams as ViewGroup.MarginLayoutParams
        closeParams.setMargins(marginPx, marginPx, marginPx, marginPx)
        closeParams.width = iconSizePx
        closeParams.height = iconSizePx
        holder.close.layoutParams = closeParams

        // Apply margin and text size to count text
        val countParams = holder.count.layoutParams as ViewGroup.MarginLayoutParams
        countParams.setMargins(marginPx, marginPx, marginPx, marginPx)
        holder.count.layoutParams = countParams
        holder.count.textSize = countSizeSp
    }

    override fun getItemCount(): Int = items.size
}
