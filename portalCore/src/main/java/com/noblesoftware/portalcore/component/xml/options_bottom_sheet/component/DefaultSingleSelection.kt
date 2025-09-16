package com.noblesoftware.portalcore.component.xml.options_bottom_sheet.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.model.SelectOption
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.extension.isFalse

@Composable
fun DefaultSingleSelection(
    modifier: Modifier = Modifier,
    selectOption: SelectOption,
    startContent: (@Composable () -> Unit)? = null,
    onSelected: (List<SelectOption>) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
            .clip(RoundedCornerShape(LocalDimen.current.small))
            .fillMaxWidth()
            .then(
                if (selectOption.isSelected) {
                    Modifier.background(
                        color = colorResource(
                            id = R.color.primary_plain_active_bg
                        )
                    )
                } else {
                    Modifier
                }
            )
            .then(
                if (selectOption.enabled) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(
                            color = colorResource(id = R.color.primary_plain_color),
                        ),
                        onClick = {
                            onSelected.invoke(arrayListOf(selectOption))
                        },
                    )
                } else Modifier
            )
            .padding(
                horizontal = LocalDimen.current.default,
                vertical = LocalDimen.current.regular
            ),
    ) {
        val (startIcon, text, endIcon) = createRefs()

        if (selectOption.startIcon != null) {
            Icon(
                modifier = Modifier.constrainAs(startIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                painter = painterResource(selectOption.startIcon),
                tint = colorResource(id = if (selectOption.isSelected) R.color.primary_plain_color else if (selectOption.enabled.isFalse()) R.color.primary_plain_disabled_color else R.color.text_primary),
                contentDescription = ""
            )
        } else if (startContent != null) {
            Box(
                modifier = Modifier
                    .constrainAs(startIcon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }

            ) {
                startContent.invoke()
            }
        }

        Text(
            modifier = Modifier.constrainAs(text) {
                if (selectOption.startIcon != null || startContent != null) {
                    start.linkTo(startIcon.end, margin = 12.dp)
                } else {
                    start.linkTo(parent.start)
                }
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                if (selectOption.endIcon != null) {
                    end.linkTo(endIcon.start, margin = 12.dp)
                } else {
                    end.linkTo(parent.end)
                }
                width = Dimension.fillToConstraints
            },
            text = if (selectOption.nameId != null) stringResource(
                id = selectOption.nameId
                    ?: R.string.empty_string
            ) else selectOption.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = colorResource(id = if (selectOption.isSelected) R.color.primary_plain_color else if (selectOption.enabled.isFalse()) R.color.primary_plain_disabled_color else R.color.text_primary)
            )
        )
        if (selectOption.endIcon != null) {
            Icon(
                modifier = Modifier.constrainAs(endIcon) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                painter = painterResource(selectOption.endIcon),
                tint = colorResource(id = if (selectOption.isSelected) R.color.primary_plain_color else if (selectOption.enabled.isFalse()) R.color.primary_plain_disabled_color else R.color.text_primary),
                contentDescription = ""
            )
        }
    }
}