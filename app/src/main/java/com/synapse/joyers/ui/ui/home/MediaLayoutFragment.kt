package com.synapse.joyers.ui.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.synapse.joyers.R

class MediaLayoutFragment : Fragment() {

    private lateinit var rootView: View
    private var currentLayout = "view1"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_media_layout, container, false)


        val tabContainer = rootView.findViewById<LinearLayout>(R.id.tab_container)
        val tabs = listOf("100", "200", "350", "450", "550")
        var selectedTab: TextView? = null

        tabs.forEachIndexed { index, label ->
            val tabView = layoutInflater.inflate(R.layout.tab_item, tabContainer, false) as TextView
            tabView.text = label

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            val margin10dp = (10 * resources.displayMetrics.density).toInt()
            val margin15dp = (15 * resources.displayMetrics.density).toInt()

            if (index == 0) {
                layoutParams.marginStart = margin10dp + margin15dp // 25dp
                layoutParams.marginEnd = margin15dp // 15dp
            } else if (index == tabs.size - 1) {
                layoutParams.marginEnd = margin10dp + margin15dp // 25dp
            } else {
                layoutParams.marginEnd = margin15dp // 15dp
            }

            tabView.layoutParams = layoutParams

            // Select the first tab by default (instead of "350")
            if (index == 0) {
                tabView.setBackgroundResource(R.drawable.tab_selected)
                tabView.setTextColor(Color.WHITE)
                selectedTab = tabView
            }

            tabView.setOnClickListener {
                selectedTab?.apply {
                    setBackgroundResource(R.drawable.tab_default)
                    setTextColor(Color.BLACK)
                }
                tabView.setBackgroundResource(R.drawable.tab_selected)
                tabView.setTextColor(Color.WHITE)
                selectedTab = tabView
            }

            tabContainer.addView(tabView)
        }

        // Set default colors for view1Tab and view2Tab, and load grid
        val view1Tab = rootView.findViewById<TextView>(R.id.view1_tab)
        val view2Tab = rootView.findViewById<TextView>(R.id.view2_tab)

        view1Tab.setTextColor(ContextCompat.getColor(requireContext(), R.color.golden))
        view2Tab.setTextColor(Color.parseColor("#666666"))
        loadMediaGrid(currentLayout)  // Use rootView inside

        view1Tab.setOnClickListener {
            if (currentLayout != "view1") {
                currentLayout = "view1"
                view1Tab.setTextColor(ContextCompat.getColor(requireContext(), R.color.golden))
                view2Tab.setTextColor(Color.parseColor("#666666"))
                loadMediaGrid("view1")
            }
        }

        view2Tab.setOnClickListener {
            if (currentLayout != "view2") {
                currentLayout = "view2"
                view2Tab.setTextColor(ContextCompat.getColor(requireContext(), R.color.golden))
                view1Tab.setTextColor(Color.parseColor("#666666"))
                loadMediaGrid("view2")
            }
        }

        return rootView
    }

    private fun loadMediaGrid(layoutType: String) {
        val mediaGrid = rootView.findViewById<GridLayout>(R.id.media_grid) ?: return
        mediaGrid.removeAllViews()

        val mediaItems = listOf(
            R.drawable.sample_image_1, R.drawable.sample_image_1,
            R.drawable.sample_image_1, R.drawable.sample_image_1,
            R.drawable.sample_image_1, R.drawable.sample_image_1
        )

        val columnCount = if (layoutType == "view1") 3 else 2
        val imageHeight = if (layoutType == "view1") 100 else 160

        mediaGrid.columnCount = columnCount

        for ((index, itemRes) in mediaItems.withIndex()) {
            val itemView = layoutInflater.inflate(R.layout.media_grid_item, mediaGrid, false)

            val img = itemView.findViewById<ImageView>(R.id.mediaImage)
            val close = itemView.findViewById<ImageView>(R.id.closeIcon)
            val play = itemView.findViewById<ImageView>(R.id.playIcon)
            val moreOverlay = itemView.findViewById<TextView>(R.id.moreOverlay)

            img.setImageResource(itemRes)

            if (index == 2) play.visibility = View.VISIBLE
            if (index == 5) {
                moreOverlay.visibility = View.VISIBLE
                close.visibility = View.GONE
            }

            // Set image height dynamically
            img.layoutParams.height = (imageHeight * resources.displayMetrics.density).toInt()

            // Set GridLayout params
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            itemView.layoutParams = params

            mediaGrid.addView(itemView)
    }
}
    }


