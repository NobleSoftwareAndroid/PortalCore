package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen

/**
 * The DefaultEmptyState composable is used to display an empty state message with an optional icon. It’s commonly used when there is no content to show (e.g., an empty list, no search results, etc.).
 *
 * Parameters
 * @param modifier A modifier that can be used to customize the appearance of the empty state.
 * @param title The title text to display in the empty state.
 * @param message The detailed message or description for the empty state.
 * @param icon An optional Painter representing an icon to display alongside the title and message.
 *
 * @sample com.noblesoftware.portalcore.component.compose.ExampleEmptyState
 *
 * @author VPN Android Team
 * @since 2024
 */

@Composable
fun DefaultEmptyState(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    icon: Painter? = null,
    spaceFraction: Int? = null
) {
    BoxWithConstraints(modifier = modifier) {
        val maxHeight = maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(LocalDimen.current.regular),
            verticalArrangement = if (spaceFraction != null && spaceFraction > 0) Arrangement.Top else Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (spaceFraction != null && spaceFraction > 0) {
                DefaultSpacer(height = maxHeight / spaceFraction)
            }
            icon?.let {
                Image(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .size(size = dimensionResource(id = R.dimen.default_empty_state_icon_size)),
                    painter = it,
                    contentDescription = title
                )
                DefaultSpacer(height = LocalDimen.current.medium)
            }
            Text(
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge.copy(color = colorResource(id = R.color.text_primary))
            )
            DefaultSpacer(height = LocalDimen.current.small)
            Text(
                text = message,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.text_secondary))
            )
        }
    }
}


@Composable
private fun ExampleEmptyState() {
    DefaultEmptyState(
        modifier = Modifier
            .padding(end = LocalDimen.current.regular)
            .fillMaxWidth(),
        icon = painterResource(id = R.drawable.img_announcement_empty),
        title = "Coming soon",
        message = "Overview of main features will be shown here"
    )
}