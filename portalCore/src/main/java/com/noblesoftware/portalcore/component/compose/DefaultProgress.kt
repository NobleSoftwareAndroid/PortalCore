package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R

/**
 * A Composable function that displays a circular progress indicator.
 *
 * @param modifier The modifier to apply to the progress indicator.
 */
@Composable
fun DefaultProgress(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(36.dp)
                .align(alignment = Alignment.Center),
            color = colorResource(id = R.color.primary_solid_bg),
            trackColor = colorResource(id = R.color.primary_outlined_hover_bg),
        )
    }
}