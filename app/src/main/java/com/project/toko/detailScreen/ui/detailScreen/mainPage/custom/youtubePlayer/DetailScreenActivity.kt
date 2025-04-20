package com.project.toko.detailScreen.ui.detailScreen.mainPage.custom.youtubePlayer

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

}