package com.synapse.joyers.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.synapse.joyers.R
import com.synapse.joyers.databinding.ActivitySplashVideoBinding

class SplashVideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashVideoBinding
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set window background to white
        window.setBackgroundDrawableResource(android.R.color.white)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_splash_video
        )

        // Initialize player
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        // Load video from raw
        val videoUri = "android.resource://$packageName/${R.raw.splash_video}".toUri()
        val mediaItem = MediaItem.fromUri(videoUri)
        player.setMediaItem(mediaItem)

        // Add listener to hide placeholder once video starts
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    startActivity(Intent(this@SplashVideoActivity, JoyersAuthActivity::class.java))
                    finish()
                }
            }
        })

        // Prepare and play
        player.prepare()
        player.play()
    }

    override fun onStop() {
        super.onStop()
        player.release()

    }

}
