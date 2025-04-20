package com.project.toko.detailScreen.ui.detailScreen.mainPage.custom.youtubePlayer

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

//import android.util.Log
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberUpdatedState
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.lifecycle.LifecycleOwner
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
//

//@Composable
//fun YoutubePlayer(
//    youtubeVideoId: String,
//    lifecycleOwner: LifecycleOwner,
//    modifier: Modifier
//) {
//    if (youtubeVideoId.isNotEmpty()) {
//        val iFramePlayerOption = IFramePlayerOptions.Builder()
//            .controls(1)
//            .build()
//
//        var isInitialized by remember { mutableStateOf(false) }
//        val context = LocalContext.current
//        val listener = rememberUpdatedState(
//            object : AbstractYouTubePlayerListener() {
//                override fun onError(
//                    youTubePlayer: YouTubePlayer,
//                    error: PlayerConstants.PlayerError
//                ) {
//                    Log.d("YoutubePlayer", error.name)
//                }
//
//                override fun onReady(youTubePlayer: YouTubePlayer) {
//                    youTubePlayer.loadVideo(youtubeVideoId, 0f)
//                    youTubePlayer.mute()
//                }
//            }
//        )
//
//        val playerView = remember {
//            YouTubePlayerView(context).apply {
//                enableAutomaticInitialization = false
//                lifecycleOwner.lifecycle.addObserver(this)
//                initialize(listener.value, iFramePlayerOption)
//            }
//        }
//
//        DisposableEffect(lifecycleOwner) {
//            onDispose {
//                if (!isInitialized) {
//                    playerView.release() // Освободите ресурсы YouTubePlayerView
//                    isInitialized = true
//                }
//            }
//        }
//
//        Box(
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(20.dp)
//                .clip(RoundedCornerShape(16.dp))
//        ) {
//            AndroidView({ playerView }) {}
//        }
//    }
//}


@Composable
fun YoutubePlayer(modifier: Modifier = Modifier, youtubeVideoId: String) {

    val listener = rememberUpdatedState(
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
    )
    AndroidView(modifier = modifier
        .fillMaxWidth()
        .padding(20.dp)
        .clip(RoundedCornerShape(16.dp)), factory = { context ->
        YouTubePlayerView(context).apply {
            enableAutomaticInitialization = false
            initialize(
                youTubePlayerListener = listener.value,
                playerOptions = IFramePlayerOptions.Builder()
                    .controls(1)
                    .build()
            )
        }
    })
}

