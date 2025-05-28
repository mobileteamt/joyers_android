package com.synapse.joyers.ui.ui.home.adapters

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.synapse.joyers.R
import com.synapse.joyers.componants.SegmentedCircleView
import com.synapse.joyers.models.Story
import de.hdodenhof.circleimageview.CircleImageView

class StoriesAdapter(private val stories: List<Story>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CUSTOM = 0
        private const val VIEW_TYPE_MY_STORE = 1
        private const val VIEW_TYPE_STORY = 2
    }

    private val borderColors = listOf(
        R.color.border1,
        R.color.border2,
        R.color.border3,
        R.color.border4
    )

    // ViewHolder for your custom view at position 0
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize any views if needed
    }

    // ViewHolder for "My Store" at position 1
    class MyStoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myStoreText: TextView = itemView.findViewById(R.id.txtMyStory)
        val segmentedCircle: SegmentedCircleView = itemView.findViewById(R.id.segmentedCircle)

    }

    // ViewHolder for story items starting at position 2
    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storyImage: CircleImageView = itemView.findViewById(R.id.storyImage)
        val storyName: TextView = itemView.findViewById(R.id.storyName)
        val segmentedCircle: SegmentedCircleView = itemView.findViewById(R.id.segmentedCircle)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_CUSTOM
            1 -> VIEW_TYPE_MY_STORE
            else -> VIEW_TYPE_STORY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CUSTOM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_custom_view, parent, false)
                CustomViewHolder(view)
            }
            VIEW_TYPE_MY_STORE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_my_story, parent, false)
                MyStoreViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_story, parent, false)
                StoryViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomViewHolder -> {
                // Bind data if needed for your custom view
            }
            is MyStoreViewHolder -> {
                holder.myStoreText.text = "My Story"

                // Random border color for "My Store"
                val context = holder.itemView.context
                val colorResId = borderColors.random()
                val actualColor = context.getColor(colorResId)
                holder.segmentedCircle.segmentColor = actualColor
                holder.segmentedCircle.segments = 1
                holder.segmentedCircle.gapAngle = 8f
                holder.segmentedCircle.randomizeColor = false
            }

            is StoryViewHolder -> {
                val storyPosition = position - 2
                val story = stories[storyPosition]
                holder.storyName.text = story.name
                holder.storyImage.setImageResource(story.imageResId)

                // Random border color
                val context = holder.itemView.context
                val colorResId = borderColors.random()
                val actualColor = context.getColor(colorResId)
                holder.segmentedCircle.segmentColor = actualColor
                holder.segmentedCircle.segments = story.reelCount
                holder.segmentedCircle.gapAngle = 8f
                holder.segmentedCircle.randomizeColor = false

                holder.segmentedCircle.gapAngle = 10f


//                holder.segmentedCircle.bottomIcon = ContextCompat.getDrawable(context, R.drawable.ic_location)
//                holder.segmentedCircle.bottomIconSize = 50

            }
        }
    }

    override fun getItemCount(): Int {
        // +2 for custom view and My Store
        return stories.size + 2
    }
}
