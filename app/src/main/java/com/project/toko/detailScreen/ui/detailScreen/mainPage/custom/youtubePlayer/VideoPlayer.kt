package com.project.toko.detailScreen.ui.detailScreen.mainPage.custom.youtubePlayer

import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


//
//@Composable
//fun YoutubePlayer(modifier: Modifier = Modifier, youtubeVideoId: String) {
//
//    val listener = rememberUpdatedState(
//        object : AbstractYouTubePlayerListener() {
//            override fun onError(
//                youTubePlayer: YouTubePlayer,
//                error: PlayerConstants.PlayerError
//            ) {
//                Log.d("YoutubePlayer", error.name)
//            }
//
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                youTubePlayer.loadVideo(youtubeVideoId, 0f)
//                youTubePlayer.mute()
//            }
//
//
//        }
//    )
//
//    AndroidView(modifier = modifier
//        .fillMaxWidth()
//        .padding(20.dp)
//        .clip(RoundedCornerShape(16.dp)), factory = { context ->
//        YouTubePlayerView(context).apply {
//            enableAutomaticInitialization = false
//            initialize(
//                youTubePlayerListener = listener.value,
//                playerOptions = IFramePlayerOptions.Builder()
//                    .controls(1)
//                    .fullscreen(1) // enable full screen button
//                    .build()
//            )
//        }
//    })
//}


//@Composable
//fun YoutubePlayer(
//    modifier: Modifier = Modifier,
//    youtubeVideoId: String,
//    onEnterFullScreen: (View) -> Unit,
//    onExitFullScreen: () -> Unit,
//    onYouTubePlayerView: () -> YouTubePlayerView?
//) {
//    Log.d("IDDD", youtubeVideoId)
//    val context = LocalContext.current
//    val listener = rememberUpdatedState(
//        object : AbstractYouTubePlayerListener() {
//            override fun onError(
//                youTubePlayer: YouTubePlayer,
//                error: PlayerConstants.PlayerError
//            ) {
//                Log.d("YoutubePlayer", error.name)
//            }
//
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                youTubePlayer.loadVideo(youtubeVideoId, 0f)
//            }
//
//        }
//    )
//    val playerView = remember {
//        YouTubePlayerView(context).apply {
//            enableAutomaticInitialization = false
//
//            initialize(
//                youTubePlayerListener = listener.value,
//                playerOptions = IFramePlayerOptions.Builder()
//                    .controls(1)
//                    .fullscreen(1)
//                    .build()
//            )
//
//            addFullscreenListener(object : FullscreenListener {
//                override fun onEnterFullscreen(
//                    fullscreenView: View,
//                    exitFullscreen: () -> Unit
//                ) {
//                    onEnterFullScreen(fullscreenView)
//                }
//
//                override fun onExitFullscreen() {
//                    onExitFullScreen()
//                }
//            })
//        }
//    }
//
//    AndroidView(
//        factory = {
//            playerView
//
////            YouTubePlayerView(context).apply {
////                enableAutomaticInitialization = false
////
////                initialize(
////                    youTubePlayerListener = listener.value,
////                    playerOptions = IFramePlayerOptions.Builder()
////                        .controls(1)
////                        .fullscreen(1)
////                        .build()
////                )
////
////                addFullscreenListener(object : FullscreenListener {
////                    override fun onEnterFullscreen(
////                        fullscreenView: View,
////                        exitFullscreen: () -> Unit
////                    ) {
////                        onEnterFullScreen(fullscreenView)
////                    }
////
////                    override fun onExitFullscreen() {
////                        onExitFullScreen()
////                    }
////                })
////            }
//        }, modifier = modifier
//            .fillMaxWidth()
//            .padding(20.dp)
//            .clip(RoundedCornerShape(16.dp))
//    )
//}


@Composable
fun YoutubePlayer(
    modifier: Modifier = Modifier,
    youtubeVideoId: String,
    onEnterFullScreen: (View) -> Unit,
    onExitFullScreen: () -> Unit,
    onYouTubePlayerView: (YouTubePlayerView) -> Unit
) {
    val context = LocalContext.current
    val listener = rememberUpdatedState(
        object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(youtubeVideoId, 0f)
            }

            override fun onError(
                youTubePlayer: YouTubePlayer,
                error: PlayerConstants.PlayerError
            ) {
                Log.d("YoutubePlayer", error.name)
            }
        }
    )

    AndroidView(
        factory = {
            YouTubePlayerView(context).apply {
                enableAutomaticInitialization = false

                initialize(
                    youTubePlayerListener = listener.value,
                    playerOptions = IFramePlayerOptions.Builder()
                        .controls(1)
                        .fullscreen(1)
                        .build()
                )

                addFullscreenListener(object : FullscreenListener {
                    override fun onEnterFullscreen(
                        fullscreenView: View,
                        exitFullscreen: () -> Unit
                    ) {
                        fullscreenView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        onEnterFullScreen(fullscreenView)
                    }

                    override fun onExitFullscreen() {
                        onExitFullScreen()
                    }
                })

                onYouTubePlayerView(this) // Передаём наружу
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}

