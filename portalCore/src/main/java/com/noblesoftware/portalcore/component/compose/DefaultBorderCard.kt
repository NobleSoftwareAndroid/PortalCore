package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes

/**
 * A composable function that creates a card with a border and customizable padding.
 *
 * @param modifier A [Modifier] to be applied to the card. Default is [Modifier].
 * @param horizontalPadding A [Dp] value for horizontal padding inside the card. Default is [LocalDimen.current.regular].
 * @param verticalPadding A [Dp] value for vertical padding inside the card. Default is [LocalDimen.current.medium].
 * @param content A composable lambda function that defines the content to be displayed inside the card.
 */
@Composable
fun DefaultBorderCard(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = LocalDimen.current.regular,
    verticalPadding: Dp = LocalDimen.current.medium,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = LocalShapes.medium,
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(id = R.color.neutral_outlined_border)
        ),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.background_body))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                )
        ) {
            content()
        }
    }
}