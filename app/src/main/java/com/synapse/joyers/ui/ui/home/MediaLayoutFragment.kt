

package com.synapse.joyers.ui.ui.home

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.synapse.joyers.R

class MediaLayoutFragment : Fragment() {

    private lateinit var rootView: View
    private var currentLayout = "fiveLayoutOne"
    private var lastTintedLayout: ViewGroup? = null
    private lateinit var imgFiveLayoutOne: ImageView
    private lateinit var imgFiveLayoutTwo: ImageView
    private lateinit var imgFiveLayoutThree: ImageView
    private var selectedTabValue: Int = 100
    val mediaItems = listOf(
        R.drawable.sample_image,
        R.drawable.sample_image
    )


    private lateinit var layoutTwo: LinearLayout
    private lateinit var layoutThree: LinearLayout
    private lateinit var layoutFour: LinearLayout
    private lateinit var layoutFive: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_media_layout, container, false)

        setupLayoutImage()

        with(rootView) {
            imgFiveLayoutOne = findViewById(R.id.imgFiveLayoutOne)
            imgFiveLayoutTwo = findViewById(R.id.imgFiveLayoutTwo)
            imgFiveLayoutThree = findViewById(R.id.imgFiveLayoutThree)

            setupTabs(findViewById(R.id.tab_container))

            val layouts = mapOf(
                "fiveLayoutOne" to findViewById<LinearLayout>(R.id.fiveLayoutOne),
                "fiveLayoutTwo" to findViewById<LinearLayout>(R.id.fiveLayoutTwo),
                "fiveLayoutThree" to findViewById<LinearLayout>(R.id.fiveLayoutThree)
            )

            layouts.forEach { (layoutName, layoutView) ->
                layoutView.setOnClickListener {
                    if (currentLayout != layoutName) {
                        switchLayout(layoutName, layoutView)
                    }
                }
            }

            // Initial selection
            switchLayout("fiveLayoutOne", layouts["fiveLayoutOne"]!!)
        }

        return rootView
    }

    private fun setupLayoutImage() {
        val value = mediaItems.size

        with(rootView) {
            layoutTwo = findViewById(R.id.twoUploads)
            layoutThree = findViewById(R.id.threeUploads)
            layoutFour = findViewById(R.id.fourUploads)
            layoutFive = findViewById(R.id.fiveUploads)
        }

        // Hide all predefined layouts initially
        val allLayouts = listOf(layoutTwo, layoutThree, layoutFour, layoutFive)
        allLayouts.forEach { it.visibility = View.GONE }

        val layouts = listOf(
            2 to layoutTwo,
            3 to layoutThree,
            4 to layoutFour,
            5 to layoutFive
        )
        layouts.forEach { (v, layout) ->
            layout.visibility = if (v == value) View.VISIBLE else View.GONE
        }
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

            // Default select 100
            if (label == "100") {
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
            "fiveLayoutOne" to Pair(imgFiveLayoutOne, R.drawable.ic_view_one_selected),
            "fiveLayoutTwo" to Pair(imgFiveLayoutTwo, R.drawable.ic_view_two_selected),
            "fiveLayoutThree" to Pair(imgFiveLayoutThree, R.drawable.ic_view_three_selected)
        )

        imgFiveLayoutOne.setImageResource(R.drawable.ic_view_one)
        imgFiveLayoutTwo.setImageResource(R.drawable.ic_view_two)
        imgFiveLayoutThree.setImageResource(R.drawable.ic_view_three)

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
        Log.e("TAG", "loadMediaGrid: $selectedTabValue")

        // Case: selectedTabValue == 100
        if (selectedTabValue == 100) {
            mediaContainer.visibility = View.VISIBLE
            mediaContainer.orientation = LinearLayout.HORIZONTAL
            mediaContainer.layoutParams.height = selectedHeightPx
            mediaContainer.requestLayout()

            val maxVisibleItems = 3
            val totalItems = mediaItems.size
            val displayCount = if (totalItems > maxVisibleItems) maxVisibleItems else totalItems

            for (i in 0 until displayCount) {
                val itemView = layoutInflater.inflate(R.layout.media_grid_item, mediaContainer, false)

                itemView.layoutParams = if (displayCount == 1) {
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                } else {
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                }

                setupItemView(itemView, mediaItems[i], density, selectedHeightPx)

                val overlay = itemView.findViewById<TextView>(R.id.moreOverlay)
                if (i == 2 && totalItems > maxVisibleItems) {
                    overlay.visibility = View.VISIBLE
                    overlay.text = "+${totalItems - maxVisibleItems}"
                    overlay.setBackgroundColor(Color.parseColor("#80000000"))
                    overlay.setTextColor(Color.WHITE)
                    overlay.textSize = 20f
                    overlay.gravity = Gravity.CENTER
                } else {
                    overlay.visibility = View.GONE
                }

                mediaContainer.addView(itemView)
            }
            return
        }

        // Case: selectedTabValue != 100 AND only 1 image
        if (mediaItems.size == 1) {
            mediaContainer.visibility = View.VISIBLE
            mediaContainer.orientation = LinearLayout.HORIZONTAL
            mediaContainer.layoutParams.height = selectedHeightPx
            mediaContainer.requestLayout()

            val itemView = layoutInflater.inflate(R.layout.media_grid_item, mediaContainer, false)
            itemView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            setupItemView(itemView, mediaItems[0], density, selectedHeightPx)

            val overlay = itemView.findViewById<TextView>(R.id.moreOverlay)
            overlay.visibility = View.GONE

            mediaContainer.addView(itemView)
            return
        }

        // Remaining logic for multiple items
        if (layoutType == "fiveLayoutOne" || layoutType == "fiveLayoutTwo") {
            mediaContainer.visibility = View.VISIBLE
            mediaContainer.layoutParams.height = selectedHeightPx
            mediaContainer.requestLayout()
            mediaContainer.orientation = LinearLayout.VERTICAL

            val firstRowWeight: Float
            val secondRowWeight: Float

            if (layoutType == "fiveLayoutTwo") {
                firstRowWeight = 0.6f
                secondRowWeight = 0.4f
            } else {
                firstRowWeight = 0.5f
                secondRowWeight = 0.5f
            }

            val firstRow = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    firstRowWeight
                )
                weightSum = 2f
            }

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

            val firstRowItemCount = minOf(2, mediaItems.size)
            for (i in 0 until firstRowItemCount) {
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
                itemView.layoutParams = params
                firstRow.addView(itemView)
            }

            for (i in 2 until mediaItems.size.coerceAtMost(5)) {
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
                itemView.layoutParams = params
                secondRow.addView(itemView)
            }

            mediaContainer.addView(firstRow)
            mediaContainer.addView(secondRow)

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
        } else if (layoutType == "fiveLayoutThree") {
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