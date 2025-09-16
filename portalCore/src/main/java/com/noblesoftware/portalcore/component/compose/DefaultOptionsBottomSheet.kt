package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.model.SelectOption
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultOptionsBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    optionType: OptionType = OptionType.SingleSelect,
    title: String = stringResource(id = R.string.empty_string),
    message: String = stringResource(id = R.string.empty_string),
    positiveButtonText: String = stringResource(id = R.string.empty_string),
    onPositive: (() -> Unit)? = null,
    onNegative: (() -> Unit)? = null,
    negativeButtonText: String = stringResource(id = R.string.empty_string),
    onDismissRequest: () -> Unit,
    options: List<SelectOption>? = null,
    onSelectOption: ((Int) -> Unit)? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {

    val bottomInsets = WindowInsets(bottom = rememberDimensionByName("navigation_bar_height"))

    ModalBottomSheet(
        modifier = modifier.imePadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = LocalDimen.current.extraRegular,
            topEnd = LocalDimen.current.extraRegular
        ),
        containerColor = colorResource(id = R.color.background_body),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        dragHandle = {
            Surface(
                modifier = modifier
                    .padding(vertical = LocalDimen.current.medium),
                color = colorResource(id = R.color.neutral_outlined_active_bg),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Box(Modifier.size(width = 56.dp, height = 4.dp))
            }
        }
    ) {
        when (optionType) {
            OptionType.SingleSelect -> {
                if (title.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = LocalDimen.current.regular),
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = title,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = 18.sp, color = colorResource(
                                    id = R.color.text_primary
                                )
                            ),
                        )
                        DefaultSpacer(height = LocalDimen.current.small)
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = message,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = colorResource(
                                    id = R.color.text_primary
                                )
                            ),
                        )
                        DefaultSpacer(height = LocalDimen.current.extraRegular)
                    }
                }
                if (options.orEmpty().isNotEmpty()) {
                    LazyColumn(
                        contentPadding = PaddingValues(LocalDimen.current.regular)
                    ) {
                        itemsIndexed(options.orEmpty()) { index, item ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = LocalShapes.medium,
                                color = colorResource(id = if (item.isSelected) R.color.primary_plain_active_bg else R.color.background_surface)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .clickable {
                                            onSelectOption?.invoke(index)
                                        }
                                        .padding(
                                            horizontal = LocalDimen.current.medium,
                                            vertical = LocalDimen.current.regular,
                                        ),
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colorResource(id = if (item.isSelected) R.color.primary_plain_color else R.color.text_primary)
                                    )
                                )
                            }
                        }
                    }
                }

                Buttons(positiveButtonText, negativeButtonText, onNegative, onPositive)
            }

            else -> {
                content?.let {
                    it()
                }
            }
        }
        DefaultSpacer(height = bottomInsets.asPaddingValues().calculateBottomPadding())
    }
}

@Composable
private fun Buttons(
    positiveButtonText: String,
    negativeButtonText: String,
    onNegative: (() -> Unit)?,
    onPositive: (() -> Unit)?
) {
    if (stringResource(id = R.string.empty_string) != positiveButtonText && stringResource(id = R.string.empty_string) != negativeButtonText) {
        HorizontalDivider(color = colorResource(id = R.color.neutral_outlined_border))
        Row(
            modifier = Modifier
                .padding(LocalDimen.current.regular)
        ) {
            if (stringResource(id = R.string.empty_string) != negativeButtonText) {
                onNegative?.let {
                    DefaultButton(
                        modifier = Modifier.weight(1f),
                        text = negativeButtonText,
                        buttonVariant = ButtonVariant.Neutral,
                        buttonSize = ButtonSize.Large,
                        buttonType = ButtonType.Outlined,
                        onClick = it::invoke
                    )
                }
                DefaultSpacer(width = LocalDimen.current.medium)
            }
            if (stringResource(id = R.string.empty_string) != positiveButtonText) {
                onPositive?.let {
                    DefaultButton(
                        modifier = Modifier.weight(1f),
                        text = positiveButtonText,
                        buttonVariant = ButtonVariant.Primary,
                        buttonSize = ButtonSize.Large,
                        onClick = it::invoke
                    )
                }
            }
        }
    }
}

@JvmInline
value class OptionType internal constructor(@Suppress("unused") private val value: Int) {

    override fun toString(): String {
        return when (this) {
            SingleSelect -> "SingleSelect"
            else -> "Invalid"
        }
    }

    companion object {
        @Stable
        val SingleSelect: OptionType = OptionType(0)
    }
}

