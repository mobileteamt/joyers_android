package com.synapse.joyers.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.synapse.joyers.R
import com.synapse.joyers.ui.ui.home.HomeFragment

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance())
                .commitNow()
        }
    }
}