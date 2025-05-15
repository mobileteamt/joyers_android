package com.synapse.joyers.ui.signup

import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.synapse.joyers.R
import com.synapse.joyers.databinding.ActivityIdentityBinding

class IdentityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdentityBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_identity)

        setupViewPager()

        val drawable = binding.strengthProgressBar.progressDrawable.mutate()
        val clipDrawable = (drawable as LayerDrawable).findDrawableByLayerId(android.R.id.progress)
        clipDrawable.setColorFilter("#D69E3A".toColorInt(), PorterDuff.Mode.SRC_IN)
        binding.strengthProgressBar.progressDrawable = drawable

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.pageTitle.text = getString(R.string.identity)
                        binding.pageCount.text = "1/3"
                        binding.strengthProgressBar.progress = 35
                    }

                    1 -> {
                        binding.pageTitle.text = getString(R.string.status)
                        binding.pageCount.text = "2/3"
                        binding.strengthProgressBar.progress = 70

                    }

                    2 -> {
                        binding.pageTitle.text = getString(R.string.status)
                        binding.pageCount.text = "3/3"
                        binding.strengthProgressBar.progress = 100

                    }
                }

            }
        })

    }

    private fun setupViewPager() {
        val fragmentList = listOf(
            PageOneFragment(),
            PageTwoFragment(),
            PageThreeFragment()
        )

        viewPagerAdapter = ViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = viewPagerAdapter

    }
}