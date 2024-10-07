package com.noblesoftware.portalcore.util.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

@Composable
fun <T> Flow<T>.rememberFlowWithLifecycle(): Flow<T> {
    val lifecycleOwner = LocalLifecycleOwner.current

    return remember(this, lifecycleOwner) {
        this.flowWithLifecycle(
            lifecycleOwner.lifecycle, Lifecycle.State.STARTED
        )
    }
}

fun <T1, T2, T3, R> zip(
    first: Flow<T1>, second: Flow<T2>, third: Flow<T3>, transform: suspend (T1, T2, T3) -> R
): Flow<R> = first.zip(second) { a, b -> a to b }.zip(third) { (a, b), c ->
        transform(a, b, c)
    }
