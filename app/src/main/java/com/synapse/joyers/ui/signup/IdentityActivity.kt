package com.synapse.joyers.ui.signup

import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.synapse.joyers.R
import com.synapse.joyers.apiData.ApiResultHandler
import com.synapse.joyers.apiData.response.BaseResponse
import com.synapse.joyers.apiData.response.UploadResponse
import com.synapse.joyers.apiData.response.UserResponse
import com.synapse.joyers.databinding.ActivityIdentityBinding
import com.synapse.joyers.localstore.PreferencesManager
import com.synapse.joyers.ui.auth.SplashVideoActivity
import com.synapse.joyers.ui.signup.PageOneFragment.SelectedImageType
import com.synapse.joyers.utils.showProgress
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class IdentityActivity : AppCompatActivity() {

    lateinit var binding: ActivityIdentityBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val signupViewModel: SignupViewModel by inject()
    private val preferencesManager: PreferencesManager by inject()
    private var pageNumber: Int = 0

    companion object {
        private const val PAGE_NUMBER = "page_number"

        fun newIntent(context: AppCompatActivity, pageNumber: Int): Intent {
            val intent = Intent(context, IdentityActivity::class.java)
            intent.putExtra(PAGE_NUMBER, pageNumber)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_identity)
        pageNumber = intent?.getIntExtra(PAGE_NUMBER, 0) ?: 0
        setupViewPager()

        goToPage(pageNumber)


        val drawable = binding.strengthProgressBar.progressDrawable.mutate()
        val clipDrawable = (drawable as LayerDrawable).findDrawableByLayerId(android.R.id.progress)
        //clipDrawable.setColorFilter("#D69E3A".toColorInt(), PorterDuff.Mode.SRC_IN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            clipDrawable.colorFilter =
                BlendModeColorFilter("#D69E3A".toColorInt(), BlendMode.SRC_IN)
        } else {
            @Suppress("DEPRECATION")
            clipDrawable.setColorFilter("#D69E3A".toColorInt(), PorterDuff.Mode.SRC_IN)
        }
        binding.strengthProgressBar.progressDrawable = drawable

        binding.viewPager.isUserInputEnabled = false

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.pageTitle.text = getString(R.string.identity)
                        binding.pageCount.text = "1/3"
                        binding.strengthProgressBar.progress = 35
                        binding.imageBack.visibility = View.INVISIBLE
                    }

                    1 -> {
                        binding.pageTitle.text = getString(R.string.status)
                        binding.pageCount.text = "2/3"
                        binding.strengthProgressBar.progress = 70
                        binding.imageBack.visibility = View.VISIBLE
                    }

                    2 -> {
                        binding.pageTitle.text = getString(R.string.status)
                        binding.pageCount.text = "3/3"
                        binding.strengthProgressBar.progress = 100
                        binding.imageBack.visibility = View.VISIBLE
                    }
                }

            }
        })

        binding.imageBack.setOnClickListener {
            goToPreviousPage()
        }

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

    fun goToNextPage() {
        binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
    }

    private fun goToPreviousPage() {
        binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
    }

    private fun goToPage(pageNumber: Int) {
        when (pageNumber) {
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
        binding.viewPager.setCurrentItem(pageNumber, true)

    }


}