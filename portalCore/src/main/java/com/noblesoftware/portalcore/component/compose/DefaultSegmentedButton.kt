package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SegmentedButtonDefaults.BorderWidth
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.model.SelectOption
import com.noblesoftware.portalcore.util.extension.orResourceStringEmpty

/**
 * A composable function that displays a segmented button with a default style.
 *
 * This function creates a row of buttons, where only one button can be selected at a time.
 * The appearance of the buttons (colors, borders, shapes) is predefined.
 *
 * @param modifier Optional [Modifier] to be applied to the segmented button row.
 * @param items A list of [SelectOption] objects representing the items to be displayed as buttons.
 *              Each [SelectOption] contains information like the display name and whether it's initially selected.
 * @param onItemClick A lambda function that will be invoked when a button is clicked.
 *                    It receives the clicked [SelectOption] as a parameter.
 *
 * @see SelectOption
 * @see SingleChoiceSegmentedButtonRow
 * @see SegmentedButton
 *
 * Created by Hafizh Anbiya on 08 July 2025
 * https://github.com/Fizhu
 */

@Composable
fun DefaultSegmentedButton(
    modifier: Modifier = Modifier,
    items: List<SelectOption>,
    onItemClick: (item: SelectOption) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
    ) {
        val colors = SegmentedButtonDefaults.colors().copy(
            activeContainerColor = colorResource(R.color.background_body),
            inactiveContainerColor = colorResource(R.color.segmented_selected_background)
        )
        items.forEachIndexed { index, item ->
            SegmentedButton(
                selected = item.isSelected,
                label = {
                    Text(
                        text = item.name.ifBlank { stringResource(item.nameId.orResourceStringEmpty()) },
                        style = if (item.isSelected) MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.W600,
                            color = colorResource(
                                R.color.primary_solid_bg
                            )
                        ) else MaterialTheme.typography.bodyMedium.copy(
                            color = colorResource(
                                R.color.text_icon
                            )
                        )
                    )
                },
                colors = colors,
                border = BorderStroke(
                    width = BorderWidth,
                    color = colorResource(R.color.divider)
                ),
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )

                    items.lastIndex -> RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    )

                    else -> RoundedCornerShape(0.dp)
                },
                icon = {},
                onClick = {
                    onItemClick(item)
                }
            )
        }
    }
}