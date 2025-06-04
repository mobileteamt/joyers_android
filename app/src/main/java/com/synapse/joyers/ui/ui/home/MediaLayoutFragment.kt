package com.synapse.joyers.ui.ui.home

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
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
import androidx.viewpager2.widget.ViewPager2
import com.synapse.joyers.R

class MediaLayoutFragment : Fragment() {

    private lateinit var rootView: View
    private var currentLayout = "layoutOne"
    private var lastTintedLayout: ViewGroup? = null
    private lateinit var imgLayoutOne: ImageView
    private lateinit var imgLayoutTwo: ImageView
    private lateinit var imgLayoutThree: ImageView
    private var selectedTabValue: Int = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_media_layout, container, false)

        with(rootView) {
            imgLayoutOne = findViewById(R.id.imgLayoutOne)
            imgLayoutTwo = findViewById(R.id.imgLayoutTwo)
            imgLayoutThree = findViewById(R.id.imgLayoutThree)

            setupTabs(findViewById(R.id.tab_container))

            val layouts = mapOf(
                "layoutOne" to findViewById<LinearLayout>(R.id.layoutOne),
                "layoutTwo" to findViewById<LinearLayout>(R.id.layoutTwo),
                "layoutThree" to findViewById<LinearLayout>(R.id.layoutThree)
            )

            layouts.forEach { (layoutName, layoutView) ->
                layoutView.setOnClickListener {
                    if (currentLayout != layoutName) {
                        switchLayout(layoutName, layoutView)
                    }
                }
            }

            // Initial selection
            switchLayout("layoutOne", layouts["layoutOne"]!!)
        }

        return rootView
    }

    private fun setupTabs(tabContainer: LinearLayout) {
        val tabs = listOf("100", "200", "350", "450", "550")
        var selectedTab: TextView? = null

        tabs.forEachIndexed { index, label ->
            val tabView = layoutInflater.inflate(R.layout.tab_item, tabContainer, false) as TextView
            tabView.text = label

            val margin10dp = (10 * resources.displayMetrics.density).toInt()
            val margin15dp = (15 * resources.displayMetrics.density).toInt()

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = if (index == 0) margin10dp + margin15dp else 0
                marginEnd = if (index == tabs.lastIndex) margin10dp + margin15dp else margin15dp
            }

            tabView.layoutParams = layoutParams

            // Default select 350
            if (label == "350") {
                tabView.setBackgroundResource(R.drawable.tab_selected)
                tabView.setTextColor(Color.WHITE)
                selectedTab = tabView
                selectedTabValue = label.toInt()
            }

            tabView.setOnClickListener {
                selectedTab?.apply {
                    setBackgroundResource(R.drawable.tab_default)
                    setTextColor(Color.BLACK)
                }
                tabView.setBackgroundResource(R.drawable.tab_selected)
                tabView.setTextColor(Color.WHITE)
                selectedTab = tabView

                selectedTabValue = label.toInt()
                loadMediaGrid(currentLayout)
            }

            tabContainer.addView(tabView)
        }

        // Ensure media grid loads with default selected tab
        loadMediaGrid(currentLayout)
    }

    private fun switchLayout(layoutType: String, layoutView: ViewGroup) {
        currentLayout = layoutType

        val golden = ContextCompat.getColor(requireContext(), R.color.golden)

        lastTintedLayout?.let { clearTintFromLayout(it) }
        applyTintToLayout(layoutView, golden)
        lastTintedLayout = layoutView

        updateLayoutIcons(layoutType)
        loadMediaGrid(layoutType)
    }

    private fun updateLayoutIcons(selected: String) {
        val icons = mapOf(
            "layoutOne" to Pair(imgLayoutOne, R.drawable.ic_view_one_selected),
            "layoutTwo" to Pair(imgLayoutTwo, R.drawable.ic_view_two_selected),
            "layoutThree" to Pair(imgLayoutThree, R.drawable.ic_view_three_selected)
        )

        imgLayoutOne.setImageResource(R.drawable.ic_view_one)
        imgLayoutTwo.setImageResource(R.drawable.ic_view_two)
        imgLayoutThree.setImageResource(R.drawable.ic_view_three)

        icons[selected]?.first?.setImageResource(icons[selected]!!.second)
    }

    private fun applyTintToLayout(root: ViewGroup, tintColor: Int) {
        for (i in 0 until root.childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                applyTintToLayout(child, tintColor)
            } else {
                child.background?.mutate()?.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)
                if (child is TextView) child.setTextColor(tintColor)
            }
        }
    }

    private fun clearTintFromLayout(root: ViewGroup) {
        for (i in 0 until root.childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                clearTintFromLayout(child)
            } else {
                child.background?.mutate()?.clearColorFilter()
                when (child) {
                    is TextView -> child.setTextColor(Color.BLACK)
                    is ImageView -> child.clearColorFilter()
                }
            }
        }
    }

//    private fun loadMediaGrid(layoutType: String) {
//        val mediaGrid = rootView.findViewById<GridLayout>(R.id.media_grid)
//        val mediaViewPager = rootView.findViewById<ViewPager2>(R.id.media_view_pager)
//
//        mediaGrid.removeAllViews()
//        mediaViewPager.adapter = null
//
//        mediaGrid.visibility = View.GONE
//        mediaViewPager.visibility = View.GONE
//
//        val density = resources.displayMetrics.density
//        val selectedHeightPx = (selectedTabValue * density).toInt()
//
//        val mediaItems = listOf(
//            R.drawable.sample_image,
//            R.drawable.sample_two,
//            R.drawable.sample_image,
//            R.drawable.sample_two,
//            R.drawable.sample_image,
//            R.drawable.sample_two
//        )
//
//        if (layoutType == "layoutOne" || layoutType == "layoutTwo") {
//            mediaGrid.visibility = View.VISIBLE
//            mediaGrid.layoutParams.height = selectedHeightPx
//            mediaGrid.requestLayout()
//
//            mediaGrid.columnCount = 6
//
//            val (topRowHeight, bottomRowHeight) = if (layoutType == "layoutOne") {
//                Pair(selectedHeightPx / 2, selectedHeightPx / 2)
//            } else {
//                Pair((selectedHeightPx * 0.6).toInt(), (selectedHeightPx * 0.4).toInt())
//            }
//
//            mediaItems.take(5).forEachIndexed { index, itemRes ->
//                val itemView = layoutInflater.inflate(R.layout.media_grid_item, mediaGrid, false)
//
//                val img = itemView.findViewById<ImageView>(R.id.mediaImage)
//                val close = itemView.findViewById<ImageView>(R.id.closeIcon)
//                val play = itemView.findViewById<ImageView>(R.id.playIcon)
//                val moreOverlay = itemView.findViewById<TextView>(R.id.moreOverlay)
//                val count = itemView.findViewById<TextView>(R.id.imageCounter)
//
//                img.setImageResource(itemRes)
//                img.scaleType = ImageView.ScaleType.CENTER_CROP
//                img.layoutParams.height = if (index < 2) topRowHeight else bottomRowHeight
//
//                count.visibility = View.GONE
//
//                // Handle play icon
//                if (index == 2) {
//                    play.visibility = View.VISIBLE
//                    val playSizeDp =
//                        if (selectedTabValue == 100 || selectedTabValue == 200) 30 else 36
//                    val playMarginDp =
//                        if (selectedTabValue == 100 || selectedTabValue == 200) 8 else 10
//                    val playSizePx = (playSizeDp * density).toInt()
//                    val playMarginPx = (playMarginDp * density).toInt()
//                    val playParams = play.layoutParams as ViewGroup.MarginLayoutParams
//                    playParams.setMargins(playMarginPx, playMarginPx, playMarginPx, playMarginPx)
//                    playParams.width = playSizePx
//                    playParams.height = playSizePx
//                    play.layoutParams = playParams
//                } else {
//                    play.visibility = View.GONE
//                }
//
//                // Handle more overlay
//                if (index == 4 && mediaItems.size > 5) {
//                    moreOverlay.visibility = View.VISIBLE
//                    moreOverlay.text = "+${mediaItems.size - 5}"
//                } else {
//                    moreOverlay.visibility = View.GONE
//                }
//
//                close.visibility = View.VISIBLE
//                val closeMarginDp =
//                    if (selectedTabValue == 100 || selectedTabValue == 200) 10 else 15
//                val closeSizeDp = if (selectedTabValue == 100 || selectedTabValue == 200) 22 else 25
//                val closeMarginPx = (closeMarginDp * density).toInt()
//                val closeSizePx = (closeSizeDp * density).toInt()
//                val closeParams = close.layoutParams as ViewGroup.MarginLayoutParams
//                closeParams.setMargins(closeMarginPx, closeMarginPx, closeMarginPx, closeMarginPx)
//                closeParams.width = closeSizePx
//                closeParams.height = closeSizePx
//                close.layoutParams = closeParams
//
//                // Optional: set margin for count if needed (hidden here)
//                val countParams = count.layoutParams as ViewGroup.MarginLayoutParams
//                countParams.setMargins(closeMarginPx, closeMarginPx, closeMarginPx, closeMarginPx)
//                count.layoutParams = countParams
//
//                val params = GridLayout.LayoutParams().apply {
//                    width = 0
//                    height = ViewGroup.LayoutParams.WRAP_CONTENT
//                    when (index) {
//                        0 -> {
//                            columnSpec = GridLayout.spec(0, 3, 1f)
//                            rowSpec = GridLayout.spec(0)
//                        }
//
//                        1 -> {
//                            columnSpec = GridLayout.spec(3, 3, 1f)
//                            rowSpec = GridLayout.spec(0)
//                        }
//
//                        2 -> {
//                            columnSpec = GridLayout.spec(0, 2, 1f)
//                            rowSpec = GridLayout.spec(1)
//                        }
//
//                        3 -> {
//                            columnSpec = GridLayout.spec(2, 2, 1f)
//                            rowSpec = GridLayout.spec(1)
//                        }
//
//                        4 -> {
//                            columnSpec = GridLayout.spec(4, 2, 1f)
//                            rowSpec = GridLayout.spec(1)
//                        }
//                    }
//                }
//
//                itemView.layoutParams = params
//                mediaGrid.addView(itemView)
//            }
//
//        } else if (layoutType == "layoutThree") {
//            mediaViewPager.visibility = View.VISIBLE
//            mediaViewPager.layoutParams.height = selectedHeightPx
//            mediaViewPager.requestLayout()
//
//            mediaViewPager.adapter = MediaPagerAdapter(mediaItems, selectedTabValue, density)
//            mediaViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//        }
//    }


    private fun loadMediaGrid(layoutType: String) {
        val mediaContainer = rootView.findViewById<LinearLayout>(R.id.media_container)
        val mediaViewPager = rootView.findViewById<ViewPager2>(R.id.media_view_pager)

        // Remove all views and hide layouts initially
        mediaContainer.removeAllViews()
        mediaViewPager.adapter = null
        mediaContainer.visibility = View.GONE
        mediaViewPager.visibility = View.GONE

        val density = resources.displayMetrics.density
        val selectedHeightPx = (selectedTabValue * density).toInt()

        val mediaItems = listOf(
            R.drawable.sample_image,
            R.drawable.sample_two,
            R.drawable.sample_image,
            R.drawable.sample_two,
            R.drawable.sample_image,
            R.drawable.sample_two
        )

        if (layoutType == "layoutOne" || layoutType == "layoutTwo") {
            mediaContainer.visibility = View.VISIBLE
            mediaContainer.layoutParams.height = selectedHeightPx
            mediaContainer.requestLayout()
            mediaContainer.orientation = LinearLayout.VERTICAL

            // Define weights for row heights based on layout type
            val firstRowWeight: Float
            val secondRowWeight: Float

            if (layoutType == "layoutTwo") {
                firstRowWeight = 0.6f  // first row taller
                secondRowWeight = 0.4f
            } else {
                firstRowWeight = 0.5f  // second row taller
                secondRowWeight = 0.5f
            }

            // Create first row (2 items)
            val firstRow = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    firstRowWeight
                )
                weightSum = 2f
            }

            // Create second row (3 items)
            val secondRow = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    secondRowWeight
                )
                weightSum = 3f
            }

            val marginPx = (4 * density).toInt()

            // Add first two items to first row
            for (i in 0..1) {
                val itemView = layoutInflater.inflate(R.layout.media_grid_item, firstRow, false)
                val itemHeightPx = (selectedHeightPx * firstRowWeight).toInt()
                setupItemView(itemView, mediaItems[i], density, itemHeightPx)

                val play = itemView.findViewById<View>(R.id.playIcon)
                if (i == 2) {
                    play.visibility = View.VISIBLE
                    val playSizeDp = if (selectedTabValue == 100 || selectedTabValue == 200) 30 else 36
                    val playMarginDp = if (selectedTabValue == 100 || selectedTabValue == 200) 8 else 10
                    val playSizePx = (playSizeDp * density).toInt()
                    val playMarginPx = (playMarginDp * density).toInt()
                    val playParams = play.layoutParams as ViewGroup.MarginLayoutParams
                    playParams.setMargins(playMarginPx, playMarginPx, playMarginPx, playMarginPx)
                    playParams.width = playSizePx
                    playParams.height = playSizePx
                    play.layoutParams = playParams
                } else {
                    play.visibility = View.GONE
                }

                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                // Removed margin setting here, so no margins at all
                // if (i < 1) params.setMargins(0, 0, marginPx, 0)
                itemView.layoutParams = params

                firstRow.addView(itemView)
            }

// Add next three items to second row
            for (i in 2..4) {
                val itemView = layoutInflater.inflate(R.layout.media_grid_item, secondRow, false)
                val itemHeightPx = (selectedHeightPx * secondRowWeight).toInt()
                setupItemView(itemView, mediaItems[i], density, itemHeightPx)

                val play = itemView.findViewById<View>(R.id.playIcon)
                if (i == 2) {
                    play.visibility = View.VISIBLE
                    val playSizeDp = if (selectedTabValue == 100 || selectedTabValue == 200) 30 else 36
                    val playMarginDp = if (selectedTabValue == 100 || selectedTabValue == 200) 8 else 10
                    val playSizePx = (playSizeDp * density).toInt()
                    val playMarginPx = (playMarginDp * density).toInt()
                    val playParams = play.layoutParams as ViewGroup.MarginLayoutParams
                    playParams.setMargins(playMarginPx, playMarginPx, playMarginPx, playMarginPx)
                    playParams.width = playSizePx
                    playParams.height = playSizePx
                    play.layoutParams = playParams
                } else {
                    play.visibility = View.GONE
                }

                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                // Removed margin setting here too
                // if (i < 4) params.setMargins(0, 0, marginPx, 0)
                itemView.layoutParams = params

                secondRow.addView(itemView)
            }



            mediaContainer.addView(firstRow)
            mediaContainer.addView(secondRow)
            // Animate row weight change on layout switch
            // This only makes sense if you keep track of previous layout weights
            // For demo: animate from equal (0.5,0.5) to desired weights

            val animDuration = 300L

            val animator = ValueAnimator.ofFloat(0.5f, firstRowWeight)
            animator.duration = animDuration
            animator.addUpdateListener { animation ->
                val animValue = animation.animatedValue as Float
                val firstParams = firstRow.layoutParams as LinearLayout.LayoutParams
                firstParams.weight = animValue
                firstRow.layoutParams = firstParams

                val secondParams = secondRow.layoutParams as LinearLayout.LayoutParams
                secondParams.weight = 1f - animValue
                secondRow.layoutParams = secondParams
            }
            animator.start()

        } else if (layoutType == "layoutThree") {
            mediaViewPager.visibility = View.VISIBLE
            mediaViewPager.layoutParams.height = selectedHeightPx
            mediaViewPager.requestLayout()

            mediaViewPager.adapter = MediaPagerAdapter(mediaItems, selectedTabValue, density)
            mediaViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
    }


// Keep your existing setupItemView but enhanced for icon sizes & margins:

    private fun setupItemView(
        itemView: View,
        drawableRes: Int,
        density: Float,
        heightPx: Int
    ) {
        val img = itemView.findViewById<ImageView>(R.id.mediaImage)
        val close = itemView.findViewById<ImageView>(R.id.closeIcon)
        val play = itemView.findViewById<ImageView>(R.id.playIcon)
        val moreOverlay = itemView.findViewById<TextView>(R.id.moreOverlay)
        val count = itemView.findViewById<TextView>(R.id.imageCounter)

        // Set image resource and scale type
        img.setImageResource(drawableRes)
        img.scaleType = ImageView.ScaleType.CENTER_CROP
        img.layoutParams.height = heightPx

        // Icon sizes in dp
        val iconSizeDp = 24
        val iconSizePx = (iconSizeDp * density).toInt()

        // Set icon size for close and play icons
        val closeParams = close.layoutParams
        closeParams.width = iconSizePx
        closeParams.height = iconSizePx
        close.layoutParams = closeParams

        val playParams = play.layoutParams
        playParams.width = iconSizePx
        playParams.height = iconSizePx
        play.layoutParams = playParams

        // Visibility & margin tweaks
        close.visibility = View.VISIBLE
        play.visibility = View.GONE
        moreOverlay.visibility = View.GONE
        count.visibility = View.GONE

        // Optionally, add margin around close icon if needed
        val closeMargin = (8 * density).toInt()
        if (close.layoutParams is ViewGroup.MarginLayoutParams) {
            val lp = close.layoutParams as ViewGroup.MarginLayoutParams
            lp.setMargins(closeMargin, closeMargin, closeMargin, closeMargin)
            close.layoutParams = lp
        }
    }


    private fun animateHeightChange(view: View, toHeight: Int) {
        val fromHeight = view.height.takeIf { it > 0 } ?: toHeight

        val anim = ValueAnimator.ofInt(fromHeight, toHeight)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            view.layoutParams.height = value
            view.requestLayout()
        }
        anim.duration = 300
        anim.start()
    }

}