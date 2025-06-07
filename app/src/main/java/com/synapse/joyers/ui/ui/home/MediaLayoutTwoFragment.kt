package com.synapse.joyers.ui.ui.home

import android.content.Context
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.synapse.joyers.R
import com.synapse.joyers.ui.ui.home.adapters.ImageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaLayoutTwoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter
    private lateinit var rootView: View
    private lateinit var layoutTwo: LinearLayout
    private lateinit var layoutView: LinearLayout
    private lateinit var layoutThree: LinearLayout
    private lateinit var layoutFour: LinearLayout
    private lateinit var layoutFive: LinearLayout


    enum class MediaLayoutType {
        HORIZONTAL, VERTICAL, FULL, THREE_ONE, THREE_TWO, FOUR_ONE, FOUR_TWO, FIVE_ONE, FIVE_TWO
    }

    private lateinit var ivTwoByTwoOne: ImageView
    private lateinit var ivTwoByTwoTwo: ImageView
    private lateinit var ivTwoByTwoThree: ImageView

    private lateinit var ivThreeByThreeOne: ImageView
    private lateinit var ivThreeByThreeTwo: ImageView
    private lateinit var ivThreeByThreeThree: ImageView

    private lateinit var ivFourByFourOne: ImageView
    private lateinit var ivFourByFourTwo: ImageView
    private lateinit var ivFourByFourThree: ImageView

    private lateinit var ivFiveByFiveOne: ImageView
    private lateinit var ivFiveByFiveTwo: ImageView
    private lateinit var ivFiveByFiveThree: ImageView

    private lateinit var threeItemGridLayoutViewOne: LinearLayout
    private lateinit var threeItemGridLayoutViewTwo: LinearLayout
    private lateinit var fourItemGridLayoutViewTwo: LinearLayout
    private lateinit var fiveItemGridLayoutViewTwo: LinearLayout


    private lateinit var threeItemGridLayoutViewOneItemOne: ImageView
    private lateinit var threeItemGridLayoutViewOneItemTwo: ImageView
    private lateinit var threeItemGridLayoutViewOneItemThree: ImageView

    private lateinit var threeItemGridLayoutViewTwoItemOne: ImageView
    private lateinit var threeItemGridLayoutViewTwoItemTwo: ImageView
    private lateinit var threeItemGridLayoutViewTwoItemThree: ImageView


    private lateinit var fourItemGridLayoutViewTwoItemOne: ImageView
    private lateinit var fourItemGridLayoutViewTwoItemTwo: ImageView
    private lateinit var fourItemGridLayoutViewTwoItemThree: ImageView
    private lateinit var fourItemGridLayoutViewTwoItemFour: ImageView

    private lateinit var fiveItemGridLayoutViewTwoItemOne: ImageView
    private lateinit var fiveItemGridLayoutViewTwoItemTwo: ImageView
    private lateinit var fiveItemGridLayoutViewTwoItemThree: ImageView
    private lateinit var fiveItemGridLayoutViewTwoItemFour: ImageView
    private lateinit var fiveItemGridLayoutViewTwoItemFive: ImageView



    // Your sample images (put actual URLs)
    sealed class MediaItem {
        data class Image(val url: String) : MediaItem()
        data class Video(val url: String) : MediaItem()
    }

    // Example list with images and videos
    private val mediaItems = listOf<MediaItem>(
        MediaItem.Image("https://picsum.photos/200/300"),
        MediaItem.Image("https://picsum.photos/id/237/200/300"),
        MediaItem.Image("https://picsum.photos/200/300.jpg"),
        MediaItem.Image("https://picsum.photos/id/237/200/300"),
        MediaItem.Image("https://picsum.photos/200/300.jpg")
    )
    private val images: List<String> by lazy {
        mediaItems.filterIsInstance<MediaItem.Image>().map { it.url }
    }


    // Current selected height tab (dp)
    private var selectedHeight = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_media_layout_two, container, false)

        with(rootView) {
            initViews()
        }

        return rootView
    }

    private fun initViews() {

        setupLayoutImage()
        setupLayoutItems()
        recyclerView = rootView.findViewById(R.id.imageRecyclerView)
        adapter = ImageAdapter(emptyList())
        recyclerView.adapter = adapter

        setupTabs(rootView.findViewById(R.id.tab_container))
        updateRecyclerViewLayout()

    }

    private fun resetAllLayoutIcons() {
        // reset 2-image layouts
        ivTwoByTwoOne.setImageResource(R.drawable.ic_two_by_two_v)
        ivTwoByTwoTwo.setImageResource(R.drawable.ic_two_by_two_h)

        // reset 3-image layouts
        ivThreeByThreeOne.setImageResource(R.drawable.ic_three_view_one)
        ivThreeByThreeTwo.setImageResource(R.drawable.ic_three_view_two)
        ivThreeByThreeThree.setImageResource(R.drawable.ic_view_three)

        // reset 4-image layouts
        ivFourByFourOne.setImageResource(R.drawable.ic_four_view_1)
        ivFourByFourTwo.setImageResource(R.drawable.ic_four_view_2)

        // reset 5-image layouts
        ivFiveByFiveOne.setImageResource(R.drawable.ic_five_view_1)
        ivFiveByFiveTwo.setImageResource(R.drawable.ic_five_view_2)
        ivFiveByFiveThree.setImageResource(R.drawable.ic_view_three)
    }

    private fun selectFirstLayoutIcon() {
        resetAllLayoutIcons() // reset all first

        val value = mediaItems.size

        when {
            value >= 5 -> {
                ivFiveByFiveOne.setImageResource(R.drawable.ic_five_view_1_selected)
                onTabSelected(MediaLayoutType.FIVE_ONE)
            }
            value == 4 -> {
                ivFourByFourOne.setImageResource(R.drawable.ic_four_view_1_selected)
                onTabSelected(MediaLayoutType.FOUR_ONE)
            }
            value == 3 -> {
                ivThreeByThreeOne.setImageResource(R.drawable.ic_three_view_one_selected)
                onTabSelected(MediaLayoutType.THREE_ONE)
            }
            value == 2 -> {
                ivTwoByTwoOne.setImageResource(R.drawable.ic_two_by_two_v_selected)
                onTabSelected(MediaLayoutType.VERTICAL)
            }
            else -> {
                // maybe no selection if less than 2 images
            }
        }
    }


    private fun setupLayoutImage() {
        val value = mediaItems.size

        with(rootView) {
            layoutView = findViewById(R.id.layoutView)
            layoutTwo = findViewById(R.id.twoUploads)
            layoutThree = findViewById(R.id.threeUploads)
            layoutFour = findViewById(R.id.fourUploads)
            layoutFive = findViewById(R.id.fiveUploads)
        }

        // Hide all predefined layouts initially
        val allLayouts = listOf(layoutTwo, layoutThree, layoutFour, layoutFive, layoutView)
        allLayouts.forEach { it.visibility = View.GONE }

        if (selectedHeight == 100) {
            // No layout visible for tab 100
            return
        }

        when {
            value >= 5 -> {
                layoutFive.visibility = View.VISIBLE
                layoutView.visibility = View.VISIBLE
            }

            value in 2..4 -> {
                val layouts = mapOf(
                    2 to layoutTwo,
                    3 to layoutThree,
                    4 to layoutFour
                )
                layouts[value]?.visibility = View.VISIBLE
                layoutView.visibility = View.VISIBLE
            }

            else -> {
                // If value < 2, all layouts remain hidden
            }
        }
    }

    private fun setupLayoutItems() {
        with(rootView) {
            //for 2 images
            ivTwoByTwoOne = findViewById(R.id.ivTwoByTwoOne)
            ivTwoByTwoTwo = findViewById(R.id.ivTwoByTwoTwo)
            ivTwoByTwoThree = findViewById(R.id.ivTwoByTwoThree)


            //for 3 images
            ivThreeByThreeOne = findViewById(R.id.ivThreeByThreeOne)
            ivThreeByThreeTwo = findViewById(R.id.ivThreeByThreeTwo)
            ivThreeByThreeThree = findViewById(R.id.ivThreeByThreeThree)

            //for 4 images
            ivFourByFourOne = findViewById(R.id.ivFourByFourOne)
            ivFourByFourTwo = findViewById(R.id.ivFourByFourTwo)
            ivFourByFourThree = findViewById(R.id.ivFourByFourThree)


            ivFiveByFiveOne = findViewById(R.id.ivFiveByFiveOne)
            ivFiveByFiveTwo = findViewById(R.id.ivFiveByFiveTwo)
            ivFiveByFiveThree = findViewById(R.id.ivFiveByFiveThree)


            // view 1 for 3 images layout
            //Containers
            threeItemGridLayoutViewOne = findViewById(R.id.threeItemGridLayoutViewOne)
            threeItemGridLayoutViewTwo = findViewById(R.id.threeItemGridLayoutViewTwo)
            fourItemGridLayoutViewTwo = findViewById(R.id.fourItemGridLayoutViewTwo)
            fiveItemGridLayoutViewTwo = findViewById(R.id.fiveItemGridLayoutViewTwo)

            //Items
            threeItemGridLayoutViewOneItemOne = findViewById(R.id.threeItemGridLayoutViewOneItemOne)
            threeItemGridLayoutViewOneItemTwo = findViewById(R.id.threeItemGridLayoutViewOneItemTwo)
            threeItemGridLayoutViewOneItemThree =
                findViewById(R.id.threeItemGridLayoutViewOneItemThree)

            threeItemGridLayoutViewTwoItemOne = findViewById(R.id.threeItemGridLayoutViewTwoItemOne)
            threeItemGridLayoutViewTwoItemTwo = findViewById(R.id.threeItemGridLayoutViewTwoItemTwo)
            threeItemGridLayoutViewTwoItemThree =
                findViewById(R.id.threeItemGridLayoutViewTwoItemThree)


            fourItemGridLayoutViewTwoItemOne = findViewById(R.id.fourItemGridLayoutViewTwoItemOne)
            fourItemGridLayoutViewTwoItemTwo = findViewById(R.id.fourItemGridLayoutViewTwoItemTwo)
            fourItemGridLayoutViewTwoItemThree =
                findViewById(R.id.fourItemGridLayoutViewTwoItemThree)
            fourItemGridLayoutViewTwoItemFour = findViewById(R.id.fourItemGridLayoutViewTwoItemFour)



            fiveItemGridLayoutViewTwoItemOne = findViewById(R.id.fiveItemGridLayoutViewTwoItemOne)
            fiveItemGridLayoutViewTwoItemTwo = findViewById(R.id.fiveItemGridLayoutViewTwoItemTwo)
            fiveItemGridLayoutViewTwoItemThree =
                findViewById(R.id.fiveItemGridLayoutViewTwoItemThree)
            fiveItemGridLayoutViewTwoItemFour = findViewById(R.id.fiveItemGridLayoutViewTwoItemFour)
            fiveItemGridLayoutViewTwoItemFive = findViewById(R.id.fiveItemGridLayoutViewTwoItemFive)

        }

        val drawableMap = mapOf(
            ivTwoByTwoOne to Pair(R.drawable.ic_two_by_two_v, R.drawable.ic_two_by_two_v_selected),
            ivTwoByTwoTwo to Pair(R.drawable.ic_two_by_two_h, R.drawable.ic_two_by_two_h_selected),
            ivTwoByTwoThree to Pair(R.drawable.ic_view_three, R.drawable.ic_view_three_selected),


            ivThreeByThreeOne to Pair(
                R.drawable.ic_three_view_one,
                R.drawable.ic_three_view_one_selected
            ),
            ivThreeByThreeTwo to Pair(
                R.drawable.ic_three_view_two,
                R.drawable.ic_three_view_two_selected
            ),
            ivThreeByThreeThree to Pair(
                R.drawable.ic_view_three,
                R.drawable.ic_view_three_selected
            ),

            ivFourByFourOne to Pair(
                R.drawable.ic_four_view_1,
                R.drawable.ic_four_view_1_selected
            ),
            ivFourByFourTwo to Pair(
                R.drawable.ic_four_view_2,
                R.drawable.ic_four_view_2_selected
            ),
            ivFourByFourThree to Pair(R.drawable.ic_view_three, R.drawable.ic_view_three_selected),


            ivFiveByFiveOne to Pair(
                R.drawable.ic_five_view_1,
                R.drawable.ic_five_view_1_selected
            ),
            ivFiveByFiveTwo to Pair(
                R.drawable.ic_five_view_2,
                R.drawable.ic_five_view_2_selected
            ),
            ivFiveByFiveThree to Pair(R.drawable.ic_view_three, R.drawable.ic_view_three_selected)


        )

        val clickListener = View.OnClickListener { selectedView ->
            drawableMap.forEach { (imageView, drawables) ->
                imageView.setImageResource(
                    if (imageView == selectedView) drawables.second
                    else drawables.first
                )
            }

            // Call a function depending on which view was clicked
            when (selectedView) {

                // for 2 images
                ivTwoByTwoOne -> onTabSelected(MediaLayoutType.VERTICAL)
                ivTwoByTwoTwo -> onTabSelected(MediaLayoutType.HORIZONTAL)
                ivTwoByTwoThree -> onTabSelected(MediaLayoutType.FULL)

                //for 3 images
                ivThreeByThreeOne -> onTabSelected(MediaLayoutType.THREE_ONE)
                ivThreeByThreeTwo -> onTabSelected(MediaLayoutType.THREE_TWO)
                ivThreeByThreeThree -> onTabSelected(MediaLayoutType.FULL)

                //for 4 images
                ivFourByFourOne -> onTabSelected(MediaLayoutType.FOUR_ONE)
                ivFourByFourTwo -> onTabSelected(MediaLayoutType.FOUR_TWO)
                ivFourByFourThree -> onTabSelected(MediaLayoutType.FULL)

                //for 5 images
                ivFiveByFiveOne -> onTabSelected(MediaLayoutType.FIVE_ONE)
                ivFiveByFiveTwo -> onTabSelected(MediaLayoutType.FIVE_TWO)
                ivFiveByFiveThree -> onTabSelected(MediaLayoutType.FULL)

            }

        }


        drawableMap.keys.forEach { imageView ->
            imageView.setOnClickListener(clickListener)
        }

    }

    private fun onTabSelected(mediaType: MediaLayoutType) {
        updateRecyclerViewLayout(mediaType)
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
                selectedHeight = label.toInt()
            }


            tabView.setOnClickListener {
                selectedTab?.apply {
                    setBackgroundResource(R.drawable.tab_default)
                    setTextColor(Color.BLACK)
                }
                tabView.setBackgroundResource(R.drawable.tab_selected)
                tabView.setTextColor(Color.WHITE)
                selectedTab = tabView

                selectedHeight = label.toInt()
                updateRecyclerViewLayout()

                setupLayoutImage()
                adapter.updatePlayIconParams(label.toInt())

                selectFirstLayoutIcon()

            }

            tabContainer.addView(tabView)
        }

    }

    private fun updateRecyclerViewLayout(mediaType: MediaLayoutType = MediaLayoutType.FULL) {
        updateRecyclerViewHeight(selectedHeight)
        when (selectedHeight) {
            100 -> setupRecyclerForHeight100()
            200, 350, 450, 550 -> setupRecycler(selectedHeight, mediaType)
            else -> setupRecyclerForHeight100()
        }
    }


    private fun updateRecyclerViewHeight(heightDp: Int) {
        recyclerView.layoutParams = recyclerView.layoutParams.apply {
            height = dpToPx(heightDp)
        }
    }
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
    private fun setupRecyclerForHeight100() {
        val mediaCount = mediaItems.size
        val spanCount = when (mediaCount) {
            1 -> 1
            2 -> 2
            else -> 3
        }

        val layoutManager = GridLayoutManager(requireContext(), spanCount).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (mediaCount == 1) spanCount else 1
                }
            }
        }
        recyclerView.layoutManager = layoutManager

        val maxVisible = 3
        val displayItems = if (mediaCount <= maxVisible) mediaItems else mediaItems.take(maxVisible)
        val overlayCount = if (mediaCount <= maxVisible) 0 else mediaCount - (maxVisible - 1)

        val totalHeightPx = dpToPx(100)
        recyclerView.layoutParams = recyclerView.layoutParams.apply {
            height = totalHeightPx
        }

        adapter.updateMediaItems(displayItems, overlayCount, totalHeightPx, 1)
    }


    fun loadVideoThumbnailFromUrl(context: Context, url: String, imageView: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retriever = MediaMetadataRetriever().apply {
                    setDataSource(url, HashMap())
                }
                val bitmap = retriever.getFrameAtTime(1_000_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                retriever.release()

                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    imageView.setImageResource(R.drawable.sample_image)
                }
            }
        }
    }

    private fun setupRecycler(height: Int, layoutType: MediaLayoutType = MediaLayoutType.FULL) {
        val totalHeightPx = dpToPx(height)
        val mediaCount = mediaItems.size
        val maxItemsToShow = 5

        fun setViewVisibility(
            recyclerVisible: Boolean,
            threeOneVisible: Boolean,
            threeTwoVisible: Boolean,
            fourTwoVisible: Boolean,
            fiveTwoVisible: Boolean
        ) {
            recyclerView.visibility = if (recyclerVisible) View.VISIBLE else View.GONE
            threeItemGridLayoutViewOne.visibility = if (threeOneVisible) View.VISIBLE else View.GONE
            threeItemGridLayoutViewTwo.visibility = if (threeTwoVisible) View.VISIBLE else View.GONE
            fourItemGridLayoutViewTwo.visibility = if (fourTwoVisible) View.VISIBLE else View.GONE
            fiveItemGridLayoutViewTwo.visibility = if (fiveTwoVisible) View.VISIBLE else View.GONE
        }

        fun updateHeight(view: View, heightPx: Int) {
            val params = view.layoutParams
            params.height = heightPx
            view.layoutParams = params
        }

        when (layoutType) {
            MediaLayoutType.FULL -> {
                setViewVisibility(true, false, false, false, false)
                recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                updateHeight(recyclerView, totalHeightPx)

                val displayItems = if (mediaCount > maxItemsToShow) mediaItems.take(maxItemsToShow) else mediaItems
                val overlayCount = if (mediaCount > maxItemsToShow) mediaCount - maxItemsToShow else 0

                adapter.updateMediaItems(displayItems, overlayCount, totalHeightPx, 1)
            }

            MediaLayoutType.FIVE_TWO -> {
                setViewVisibility(false, false, false, false, true)
                updateHeight(fiveItemGridLayoutViewTwo, totalHeightPx)

                val views = listOf(
                    fiveItemGridLayoutViewTwoItemOne,
                    fiveItemGridLayoutViewTwoItemTwo,
                    fiveItemGridLayoutViewTwoItemThree,
                    fiveItemGridLayoutViewTwoItemFour,
                    fiveItemGridLayoutViewTwoItemFive
                )

                mediaItems.take(4).forEachIndexed { index, mediaItem ->
                    val imageView = views[index]
                    when (mediaItem) {
                        is MediaItem.Image -> Glide.with(requireContext()).load(mediaItem.url).into(imageView)
                        is MediaItem.Video -> loadVideoThumbnailFromUrl(requireContext(), mediaItem.url, imageView)
                    }
                }
            }

            MediaLayoutType.FOUR_TWO -> {
                setViewVisibility(false, false, false, true, false)
                updateHeight(fourItemGridLayoutViewTwo, totalHeightPx)

                val views = listOf(
                    fourItemGridLayoutViewTwoItemOne,
                    fourItemGridLayoutViewTwoItemTwo,
                    fourItemGridLayoutViewTwoItemThree,
                    fourItemGridLayoutViewTwoItemFour
                )

                mediaItems.take(3).forEachIndexed { index, mediaItem ->
                    val imageView = views[index]
                    when (mediaItem) {
                        is MediaItem.Image -> Glide.with(requireContext()).load(mediaItem.url).into(imageView)
                        is MediaItem.Video -> loadVideoThumbnailFromUrl(requireContext(), mediaItem.url, imageView)
                    }
                }
            }

            MediaLayoutType.THREE_ONE -> {
                setViewVisibility(false, true, false, false, false)
                updateHeight(threeItemGridLayoutViewOne, totalHeightPx)

                val views = listOf(
                    threeItemGridLayoutViewOneItemOne,
                    threeItemGridLayoutViewOneItemTwo,
                    threeItemGridLayoutViewOneItemThree
                )

                mediaItems.take(2).forEachIndexed { index, mediaItem ->
                    val imageView = views[index]
                    when (mediaItem) {
                        is MediaItem.Image -> Glide.with(requireContext()).load(mediaItem.url).into(imageView)
                        is MediaItem.Video -> loadVideoThumbnailFromUrl(requireContext(), mediaItem.url, imageView)
                    }
                }
            }

            MediaLayoutType.THREE_TWO -> {
                setViewVisibility(false, false, true, false, false)
                updateHeight(threeItemGridLayoutViewTwo, totalHeightPx)

                val views = listOf(
                    threeItemGridLayoutViewTwoItemOne,
                    threeItemGridLayoutViewTwoItemTwo,
                    threeItemGridLayoutViewTwoItemThree
                )

                mediaItems.take(2).forEachIndexed { index, mediaItem ->
                    val imageView = views[index]
                    when (mediaItem) {
                        is MediaItem.Image -> Glide.with(requireContext()).load(mediaItem.url).into(imageView)
                        is MediaItem.Video -> loadVideoThumbnailFromUrl(requireContext(), mediaItem.url, imageView)
                    }
                }
            }

            else -> {
                setViewVisibility(true, false, false, false, false)

                val totalSpanCount = 6
                val layoutManager = GridLayoutManager(requireContext(), totalSpanCount).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return when (mediaCount) {
                                1 -> 6
                                2 -> if (layoutType == MediaLayoutType.HORIZONTAL) 6 else 3
                                3 -> if (position < 2) 3 else 6
                                4 -> 3
                                else -> if (position < 2) 3 else 2
                            }
                        }
                    }
                }

                recyclerView.layoutManager = layoutManager
                updateHeight(recyclerView, totalHeightPx)

                val displayItems = if (mediaCount <= maxItemsToShow) mediaItems else mediaItems.take(maxItemsToShow)
                val overlayCount = if (mediaCount <= maxItemsToShow) 0 else mediaCount - (maxItemsToShow - 1)

                val rowCount = when {
                    mediaCount == 2 && layoutType == MediaLayoutType.HORIZONTAL -> 2
                    mediaCount <= 2 -> 1
                    else -> 2
                }

                val perRowHeightPx = totalHeightPx / rowCount
                adapter.updateMediaItems(displayItems, overlayCount, perRowHeightPx, rowCount)
            }
        }
    }

}
