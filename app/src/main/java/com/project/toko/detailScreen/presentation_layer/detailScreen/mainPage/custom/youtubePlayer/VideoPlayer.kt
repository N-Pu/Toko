package com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.youtubePlayer

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


    @Composable
    fun YoutubePlayer(
        youtubeVideoId: String,
        lifecycleOwner: LifecycleOwner,
        modifier: Modifier
    ) {
//        var isFullscreen = false

        val iFramePlayerOption = IFramePlayerOptions.Builder()
            .controls(1)
//            .fullscreen(1)
            .build()
        if (youtubeVideoId.isNotEmpty()) {
            val listener =
                object : AbstractYouTubePlayerListener() {
                    override fun onError(
                        youTubePlayer: YouTubePlayer,
                        error: PlayerConstants.PlayerError
                    ) {
                        Log.d("YoutubePlayer", error.name)
                    }

                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(youtubeVideoId, 0f)
                        youTubePlayer.mute()
                    }

                }
            AndroidView(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(16.dp)),
                factory = { context ->

                    YouTubePlayerView(context = context)
                        .apply {
                            enableAutomaticInitialization = false
                            lifecycleOwner.lifecycle.addObserver(this)
//
//                            addFullscreenListener(object : FullscreenListener {
//                                override fun onEnterFullscreen(
//                                    fullscreenView: View,
//                                    exitFullscreen: () -> Unit
//                                ) {
//                                    isFullscreen = true
//
//                                }
//
//                                override fun onExitFullscreen() {
//                                    isFullscreen = false
//                                }
//                            })

                            this.initialize(listener, iFramePlayerOption)

                        }

                }, update = {

                })




        }
    }


//@Composable
//fun YoutubePlayer(
//    youtubeVideoId: String,
//    lifecycleOwner: LifecycleOwner,
//    modifier: Modifier
//) {
//    var isFullscreen by remember { mutableStateOf(false) }

//    val iFramePlayerOption = IFramePlayerOptions.Builder()
//        .controls(1)
//        .fullscreen(1)
//        .build()

//    val fullscreenViewContainer =
//        FrameLayout(LocalContext.current)
//            .findViewById<FrameLayout>(R.id.full_screen_view_container)

//    if (youtubeVideoId.isNotEmpty()) {
//        val listener =
//            object : AbstractYouTubePlayerListener() {
//
//                override fun onError(
//                    youTubePlayer: YouTubePlayer,
//                    error: PlayerConstants.PlayerError
//                ) {
//                    Log.d("YoutubePlayer", error.name)
//                }
//
//                override fun onReady(youTubePlayer: YouTubePlayer) {
//                    youTubePlayer.loadVideo(youtubeVideoId, 0f)
//                    youTubePlayer.pause()
//                }
//            }
//
//        AndroidView(
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(20.dp)
//                .clip(RoundedCornerShape(16.dp)),
//            factory = { context ->
//                YouTubePlayerView(context = context)
//                    .apply {
//
//                        enableAutomaticInitialization = false
//                        lifecycleOwner.lifecycle.addObserver(this)
//
//
////                        val fullscreenViewContainer =
////                            findViewById<FrameLayout>(R.id.full_screen_view_container)
//
//                        val fullscreenViewContainer =
//                            FrameLayout(context).findViewById<FrameLayout>(R.id.full_screen_view_container)
//
//
//                        addFullscreenListener(object : FullscreenListener {
//                            override fun onEnterFullscreen(
//                                fullscreenView: View,
//                                exitFullscreen: () -> Unit
//                            ) {
//                                this@apply.visibility = View.VISIBLE
//                                fullscreenViewContainer.visibility = View.VISIBLE
//                                fullscreenViewContainer.addView(fullscreenView)
//
//                            }
//
//                            override fun onExitFullscreen() {
//                                this@apply.visibility = View.VISIBLE
//                                fullscreenViewContainer.visibility = View.VISIBLE
//                                fullscreenViewContainer.visibility = View.GONE
//                                fullscreenViewContainer.removeAllViews()
//                            }
//                        })
//
//                        this.initialize(listener, iFramePlayerOption)
//                    }
//            }
//        )
//
////        AndroidView(
////            modifier = modifier
////                .fillMaxSize(),
////            factory = { context ->
////
////                FrameLayout(context).apply {
////                    object : FullscreenListener {
////                        override fun onEnterFullscreen(
////                            fullscreenView: View,
////                            exitFullscreen: () -> Unit
////                        ) {
////                            this@apply.visibility = View.GONE
////                            fullscreenViewContainer.visibility = View.VISIBLE
////                            fullscreenViewContainer.addView(fullscreenView)
//////                                isFullscreen = true
////
////                        }
////
////                        override fun onExitFullscreen() {
//////                                isFullscreen = false
////                            this@apply.visibility = View.VISIBLE
////                            fullscreenViewContainer.visibility = View.VISIBLE
////                            fullscreenViewContainer.visibility = View.GONE
////                            fullscreenViewContainer.removeAllViews()
////                        }
////                    }
////                }
//
//
////                        val fullscreenViewContainer =
////                            findViewById<FrameLayout>(R.id.full_screen_view_container)
//
////
////            }
////
////        )
//
//        // Добавьте кнопку входа в полноэкранный режим
////        Button(
////            onClick = {
////                if (!isFullscreen) {
////                    // Если не в полноэкранном режиме, переключить в полноэкранный режим
////                    isFullscreen = true
////                }
////            }
////        ) {
////            Text(text = "Войти в полноэкранный режим")
////        }
//    }
//
//    val context = LocalContext.current
//
//    Button(onClick = {
//        context.startActivity(Intent(context, FullScreenYoutubeActivity::class.java))
//    }) {
//        Text(text = "Show List")
//    }
//}


