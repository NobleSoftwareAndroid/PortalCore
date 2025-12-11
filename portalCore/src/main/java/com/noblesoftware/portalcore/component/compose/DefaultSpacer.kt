package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.theme.LocalDimen

/**
 * The DefaultSpacer composable is used to insert empty space (padding) between UI elements. It allows you to control the vertical and horizontal spacing within your layout.
 *
 * Parameters
 * @param height The vertical spacing (height) to add. Defaults to the regular spacing defined by the current theme.
 * @param width The horizontal spacing (width) to add. Defaults to the regular spacing defined by the current theme.
 *
 * @sample com.noblesoftware.portalcore.component.compose.ExampleDefaultSpacer
 *
 * @author VPN Android Team
 * @since 2024
 */
@Composable
fun DefaultSpacer(
    height: Dp = LocalDimen.current.zero,
    width: Dp = LocalDimen.current.zero,
) {
    val defaultSize = LocalDimen.current.regular
    val spacerModifier = when {
        width > LocalDimen.current.zero -> Modifier.width(width)
        height > LocalDimen.current.zero -> Modifier.height(height)
        else -> Modifier
            .height(defaultSize)
            .width(defaultSize)
    }
    Spacer(modifier = spacerModifier)
}

@Composable
private fun ExampleDefaultSpacer() {
    DefaultSpacer(height = 16.dp)
    DefaultSpacer(width = 8.dp)
}