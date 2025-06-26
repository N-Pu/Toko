package com.project.toko.detailScreen.ui.detailScreen.mainPage.custom.youtubePlayer

import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@Composable
fun YoutubePlayer(
    modifier: Modifier = Modifier,
    youtubeVideoId: String,
    onEnterFullScreen: (View) -> Unit,
    onExitFullScreen: () -> Unit,
    onYouTubePlayerView: (YouTubePlayerView) -> Unit,
    onLeavingFragment: () -> Unit,
) {
    var isVisible by remember { mutableStateOf(false) }
    var currentState by remember { mutableStateOf(PlayerConstants.PlayerState.UNKNOWN) }
    val context = LocalContext.current
    val youTubePlayerRef = remember { mutableStateOf<YouTubePlayer?>(null) }
    var wasPausedByUser by rememberSaveable { mutableStateOf(false) }
    var isInFullscreenTransition by remember { mutableStateOf(false) }


    HandlePlayerState(
        isVisible = isVisible,
        currentState = currentState,
        wasPausedByUser = wasPausedByUser,
        isInFullscreenTransition = isInFullscreenTransition,
        youTubePlayerRef = youTubePlayerRef
    )

    val listener by rememberUpdatedState(
        object : AbstractYouTubePlayerListener() {
            var currentSecond = 0f

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayerRef.value = youTubePlayer
                Log.d("YoutubePlayer", "Video ID: $youtubeVideoId")
                youTubePlayer.cueVideo(youtubeVideoId, currentSecond)
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                // фиксировать паузу, поставленную пользователем
                if (currentState == PlayerConstants.PlayerState.PLAYING &&
                    state == PlayerConstants.PlayerState.PAUSED &&
                    isVisible
                ) {
                    wasPausedByUser = true
                }

                // сбрасывать флаг, если пользователь снова нажал "play"
                if (state == PlayerConstants.PlayerState.PLAYING) {
                    wasPausedByUser = false
                }

                currentState = state
                Log.d("YoutubePlayer", "State: $state")
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                currentSecond = second
            }
        }
    )

    AndroidView(
        factory = {
            YouTubePlayerView(context).apply {
                enableAutomaticInitialization = false
                initialize(
                    youTubePlayerListener = listener,
                    playerOptions = IFramePlayerOptions.Builder()
                        .controls(1)
                        .mute(1)
                        .fullscreen(1)
                        .build()
                )

                addFullscreenListener(object : FullscreenListener {
                    override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                        isInFullscreenTransition = true
                        fullscreenView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        onEnterFullScreen(fullscreenView)
                    }

                    override fun onExitFullscreen() {
                        isInFullscreenTransition = false
                        onExitFullScreen()
                    }
                })

                onYouTubePlayerView(this)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(16.dp))
            .isElementVisible { isVisible = it }
    )

    DisposableEffect(Unit) {
        onDispose { onLeavingFragment() }
    }
}



@Composable
private fun HandlePlayerState(
    isVisible: Boolean,
    currentState: PlayerConstants.PlayerState,
    wasPausedByUser: Boolean,
    isInFullscreenTransition: Boolean,
    youTubePlayerRef: MutableState<YouTubePlayer?>
) {
    LaunchedEffect(isVisible, currentState, isInFullscreenTransition) {
        val player = youTubePlayerRef.value ?: return@LaunchedEffect

        if (isInFullscreenTransition) return@LaunchedEffect

        when {
            isVisible && currentState in listOf(
                PlayerConstants.PlayerState.UNSTARTED,
                PlayerConstants.PlayerState.VIDEO_CUED
            ) && !wasPausedByUser -> {
                player.play()
            }

            isVisible && currentState == PlayerConstants.PlayerState.PAUSED && !wasPausedByUser -> {
                player.play()
            }

            !isVisible && currentState == PlayerConstants.PlayerState.PLAYING -> {
                player.pause()
            }
        }
    }
}




fun Modifier.isElementVisible(
    onVisibilityChanged: (Boolean) -> Unit
): Modifier = composed {
    var lastVisibility by remember { mutableStateOf(false) }

    this.onGloballyPositioned { coordinates ->
        val parentCoordinates = coordinates.parentLayoutCoordinates
        val isCurrentlyVisible = parentCoordinates?.let {
            val parentBounds = it.boundsInWindow()
            val childBounds = coordinates.boundsInWindow()
            parentBounds.overlaps(childBounds)
        } ?: false

        if (lastVisibility != isCurrentlyVisible) {
            lastVisibility = isCurrentlyVisible
            onVisibilityChanged(isCurrentlyVisible)
        }
    }
}


