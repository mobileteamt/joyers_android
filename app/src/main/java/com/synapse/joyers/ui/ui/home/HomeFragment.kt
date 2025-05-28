package com.synapse.joyers.ui.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synapse.joyers.R
import com.synapse.joyers.models.FeedItem
import com.synapse.joyers.models.Story
import com.synapse.joyers.ui.ui.home.adapters.FeedAdapter
import com.synapse.joyers.ui.ui.home.adapters.StoriesAdapter
import com.synapse.joyers.utils.DimensionUtils


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerStories: RecyclerView = view.findViewById(R.id.recyclerStories)

        val stories = listOf(
            Story("Ellis edwards", R.drawable.img_story_2, 1),
            Story("James", R.drawable.img_story_3, 2),
            Story("Eric", R.drawable.img_story_4, 4),
        )

        recyclerStories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerStories.adapter = StoriesAdapter(stories)

        val recyclerFeed: RecyclerView = view.findViewById(R.id.recyclerFeed)
        recyclerFeed.layoutManager = LinearLayoutManager(context)
        val dummyData = listOf(
            FeedItem(
                title = "Sara Spiegel James Spie Jame Sit Sit",
                type = "Software Developer",
                rating = 1,
                timestamp = "29 Jan 20, 6:30 AM",
                location = "Noida",
                description = "Hi there! I’m using the best platform on this globe — it’s Joyers Network! It’s an amazing social platform that represents EVERY single aspect of my life! I’ve recommended it to many of my friends who love creating businesses, enjoying hobbies, and entertainment! Guys, let’s join the Joyers Network!",
                imageResId = R.drawable.img_story
            ),
            FeedItem(
                title = "Mercedes Global",
                type = "Software Developer",
                rating = 2,
                timestamp = "29 Jan 20, 6:30 AM",
                location = "Noida",
                description = "Hi there! I’m using the best platform on this globe — it’s Joyers Network! It’s an amazing social platform that represents EVERY single aspect of my life! I’ve recommended it to many of my friends who love creating businesses, enjoying hobbies, and entertainment! Guys, let’s join the Joyers Network!",
                imageResId = R.drawable.img_profile_two
            ),
            FeedItem(
                title = "Mercedes Global",
                type = "Software Developer",
                rating = 3,
                timestamp = "29 Jan 20, 6:30 AM",
                location = "Noida",
                description = "Hi there! I’m using the best platform on this globe — it’s Joyers Network! It’s an amazing social platform that represents EVERY single aspect of my life! I’ve recommended it to many of my friends who love creating businesses, enjoying hobbies, and entertainment! Guys, let’s join the Joyers Network!",
                imageResId = R.drawable.img_story_2
            ),
            FeedItem(
                title = "Mercedes Global",
                type = "Software Developer",
                rating = 4,
                timestamp = "29 Jan 20, 6:30 AM",
                location = "Noida",
                description = "Hi there! I’m using the best platform on this globe — it’s Joyers Network! It’s an amazing social platform that represents EVERY single aspect of my life! I’ve recommended it to many of my friends who love creating businesses, enjoying hobbies, and entertainment! Guys, let’s join the Joyers Network!",
                imageResId = R.drawable.img_story_3
            ),
            FeedItem(
                title = "Mercedes Global",
                type = "Software Developer",
                rating = 5,
                timestamp = "29 Jan 20, 6:30 AM",
                location = "Noida",
                description = "Hi there! I’m using the best platform on this globe — it’s Joyers Network! It’s an amazing social platform that represents EVERY single aspect of my life! I’ve recommended it to many of my friends who love creating businesses, enjoying hobbies, and entertainment! Guys, let’s join the Joyers Network!",
                imageResId = R.drawable.img_story_4
            ),
        )

        recyclerFeed.adapter = FeedAdapter(dummyData)


        val btnOpenFragment: ConstraintLayout = view.findViewById(R.id.headerRow)

        btnOpenFragment.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, MediaLayoutFragment())
                .addToBackStack(null)
                .commit()
        }



        return view
    }
}
