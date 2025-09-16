package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes

/**
 * Created by Hafizh Anbiya on 22 May 2024
 * https://github.com/Fizhu
 */

/**
 * A Composable function that creates a default chip selection UI component.
 *
 * @param modifier The Modifier to be applied to the chip.
 * @param label The text to be displayed on the chip.
 * @param selected A boolean indicating whether the chip is in a selected state. Default is false.
 * @param onClick A lambda function that will be triggered when the chip is clicked.
 */
@Composable
fun DefaultChipSelection(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val textColor =
        colorResource(id = if (selected) R.color.primary_soft_color else R.color.neutral_solid_active_bg)
    val backgroundColor =
        colorResource(id = if (selected) R.color.primary_soft_bg else R.color.background_body)
    Row(
        modifier = modifier
            .clip(LocalShapes.medium.copy(all = CornerSize(LocalDimen.current.extraRegular)))
            .border(
                width = 1.dp,
                color = colorResource(id = if (selected) R.color.primary_soft_bg else R.color.danger_solid_disabled_bg),
                shape = LocalShapes.medium.copy(all = CornerSize(LocalDimen.current.extraRegular))
            )
            .background(backgroundColor)
            .clickable { onClick.invoke() }
            .padding(
                horizontal = LocalDimen.current.default, vertical = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "Checked",
                tint = colorResource(
                    id = R.color.primary_soft_color
                )
            )
            DefaultSpacer(width = LocalDimen.current.small.plus(2.dp))
        }
        Box(modifier = Modifier.height(LocalDimen.current.extraRegular))
        Text(
            text = label, style = MaterialTheme.typography.bodyMedium.copy(
                color = textColor, fontWeight = FontWeight.W500
            ), maxLines = 1, overflow = TextOverflow.Ellipsis
        )
    }
}