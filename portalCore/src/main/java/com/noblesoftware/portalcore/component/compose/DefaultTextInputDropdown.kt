package com.noblesoftware.portalcore.component.compose

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes
import com.noblesoftware.portalcore.util.extension.isTrue

@Preview
@Composable
fun DefaultTextInputDropdownPreview() {
    DefaultTextInputDropdown(
        label = "",
        value = "",
        onClick = {},
        onValueChange = {}
    )
}

@Composable
fun DefaultTextInputDropdown(
    isWrapContent: Boolean = false,
    label: String = stringResource(id = R.string.empty_string),
    value: String,
    placeholder: String = stringResource(id = R.string.empty_string),
    isOpen: Boolean? = false,
    enabled: Boolean = true,
    required: Boolean = false,
    errorText: String = stringResource(id = R.string.empty_string),
    @DrawableRes icon: Int = R.drawable.ic_expand_more_filled,
    @ColorRes iconTint: Int = R.color.text_icon,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = {
        DefaultTextInputIcon(
            modifier = Modifier
                .rotate(if (isOpen.isTrue()) 180f else 0f),
            icon = painterResource(id = icon),
            tint = iconTint
        )
    },
    onClick: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .then(
                if (isWrapContent) Modifier.width(IntrinsicSize.Min) else Modifier
            )
            .disabledHorizontalPointerInputScroll(true)
    ) {
        DefaultTextInput(
            readOnly = true,
            enabled = enabled,
            label = label,
            value = value,
            placeholder = placeholder,
            inputType = KeyboardType.Text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            errorText = errorText,
            onValueChange = onValueChange,
            required = required
        )
        Column(
            modifier = Modifier
                .matchParentSize()
                .padding(
                    start = LocalDimen.current.extraSmall,
                    end = LocalDimen.current.extraSmall,
                    bottom = if (errorText.isNotEmpty()) 26.dp else LocalDimen.current.extraSmall,
                    top = if (label.isNotEmpty()) LocalDimen.current.extraRegular else LocalDimen.current.extraSmall,
                )
        ) {
            Box(
                modifier = Modifier
                    .clip(LocalShapes.medium)
                    .fillMaxSize()
                    .then(
                        if (enabled) {
                            Modifier.clickable {
                                onClick()
                            }
                        } else Modifier
                    )
            )
        }
    }
}

private val horizontalScrollConsumer = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(y = 0f)
    override suspend fun onPreFling(available: Velocity) = available.copy(y = 0f)
}

fun Modifier.disabledHorizontalPointerInputScroll(disabled: Boolean = true) =
    if (disabled) this.nestedScroll(horizontalScrollConsumer) else this