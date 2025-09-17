package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A Composable function that creates a Box with swipe-to-refresh functionality.
 *
 * @param modifier The Modifier to be applied to the Box.
 * @param onSwipe A lambda function that will be triggered when a swipe gesture is detected.
 * @param isRefreshing A boolean indicating whether the refresh animation should be displayed.
 * @param content A Composable function that defines the content to be displayed within the Box.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxWithSwipeRefresh(
    modifier: Modifier = Modifier,
    onSwipe: () -> Unit,
    isRefreshing: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    PullToRefreshBox(modifier = Modifier, isRefreshing = isRefreshing, onRefresh = { onSwipe() }) {
        content()
    }
}