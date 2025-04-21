package com.project.toko.detailScreen.ui.detailScreen.mainPage.custom.youtubePlayer

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.project.toko.R


class DetailScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.detail_screen_activity)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.youtube_player_view)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        val username = intent.getStringExtra("id") ?: ""
        Log.d("CURRENT ID IN DETAIL ACTIVITY", username)
    }

//    lateinit var  youTubePlayerView: YouTubePlayerView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(com.project.toko.R.layout.detail_screen_activity)
//
//        youTubePlayerView = findViewById(com.project.toko.R.id.youtube_player_view)
//        lifecycle.addObserver(youTubePlayerView)
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
//            youTubePlayerView!!.matchParent()
//        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
//            youTubePlayerView!!.wrapContent()
//        }
//    }


    private lateinit var youTubePlayer: YouTubePlayer

    private var isFullscreen = false
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                // if the player is in fullscreen, exit fullscreen
                youTubePlayer.toggleFullscreen()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.detail_screen_activity)

        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        val fullscreenViewContainer = findViewById<FrameLayout>(R.id.full_screen_view_container)

        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1) // enable full screen button
            .build()

        // we need to initialize manually in order to pass IFramePlayerOptions to the player
        youTubePlayerView.enableAutomaticInitialization = false

        youTubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                // the video will continue playing in fullscreenView
                youTubePlayerView.visibility = View.GONE
                fullscreenViewContainer.visibility = View.VISIBLE
                fullscreenViewContainer.addView(fullscreenView)

                // optionally request landscape orientation
                // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            override fun onExitFullscreen() {
                isFullscreen = false

                // the video will continue playing in the player
                youTubePlayerView.visibility = View.VISIBLE
                fullscreenViewContainer.visibility = View.GONE
                fullscreenViewContainer.removeAllViews()
            }
        })

        youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                Log.d("NNNNNN", "27OZc-ku6is")
                this@DetailScreenActivity.youTubePlayer = youTubePlayer
                youTubePlayer.loadVideo("27OZc-ku6is", 0f)

                val enterFullscreenButton = findViewById<Button>(R.id.enter_fullscreen_button)
                enterFullscreenButton.setOnClickListener {
                    youTubePlayer.toggleFullscreen()
                }
            }
        }, iFramePlayerOptions)

        lifecycle.addObserver(youTubePlayerView)
    }
}