package com.synapse.joyers.ui.signup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    activity: FragmentActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]

    fun getFragmentAt(position: Int): Fragment = fragments[position]

}