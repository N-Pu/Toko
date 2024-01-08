package com.project.toko.core.presentation_layer.pullToRefpresh

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
fun PullToRefreshLayout(
    composable: @Composable (() -> Unit),
    onLoad: () -> Unit,
    swipeRefreshState: SwipeRefreshState
) {
    SwipeRefresh(indicator = { state, refreshTrigger ->
        SwipeRefreshIndicator(
            state = state,
            refreshTriggerDistance = refreshTrigger,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    }, state = swipeRefreshState, onRefresh =
    {
        onLoad()
    }
    ) {
        composable()
    }
}