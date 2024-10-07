package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.extension.fadingEdge
import com.noblesoftware.portalcore.util.extension.ifNullOrEmpty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultDurationPicker(
    durationPickerState: DurationPickerState,
    title: String = stringResource(id = R.string.empty_string),
    hoursText: String = stringResource(id = R.string.empty_string),
    minsText: String = stringResource(id = R.string.empty_string),
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = colorResource(id = R.color.background_body),
            disabledDayContentColor = colorResource(id = R.color.neutral_solid_disabled_color)
        ),
        onDismissRequest = {
            onDismiss.invoke()
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.padding(
                    end = LocalDimen.current.default,
                    bottom = LocalDimen.current.default
                ),
                onClick = {
                    onConfirm.invoke()
                    onDismiss.invoke()
                },
            ) {
                Text(text = confirmText, color = colorResource(id = R.color.primary_solid_bg))
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.padding(bottom = LocalDimen.current.default),
                onClick = {
                    onDismiss.invoke()
                }) {
                Text(text = dismissText, color = colorResource(id = R.color.primary_solid_bg))
            }
        }
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(color = colorResource(id = R.color.text_secondary)),
            modifier = Modifier.padding(
                start = LocalDimen.current.extraRegular,
                top = LocalDimen.current.regular.plus(LocalDimen.current.small)
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = LocalDimen.current.extraRegular,
                        vertical = LocalDimen.current.regular
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Picker(
                    modifier = Modifier.width(64.dp),
                    state = durationPickerState,
                    items = durationPickerState.hoursItems.value,
                    itemType = DefaultDurationType.HOURS,
                    visibleItemsCount = 3,
                    textModifier = Modifier.padding(LocalDimen.current.regular),
                    textStyle = MaterialTheme.typography.labelLarge.copy(color = colorResource(id = R.color.text_primary))
                )
                Text(
                    text = hoursText,
                    style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.text_secondary)),
                )
                DefaultSpacer(width = LocalDimen.current.regular)
                Picker(
                    modifier = Modifier.width(64.dp),
                    state = durationPickerState,
                    items = durationPickerState.minutesItems.value,
                    itemType = DefaultDurationType.MINUTE,
                    visibleItemsCount = 3,
                    textModifier = Modifier.padding(LocalDimen.current.regular),
                    textStyle = MaterialTheme.typography.labelLarge.copy(color = colorResource(id = R.color.text_primary))
                )
                Text(
                    text = minsText,
                    style = MaterialTheme.typography.bodyMedium.copy(color = colorResource(id = R.color.text_secondary)),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Picker(
    modifier: Modifier = Modifier,
    items: List<String>,
    itemType: DefaultDurationType,
    state: DurationPickerState,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = colorResource(id = R.color.primary_plain_color),
) {

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = items.size

    val listState =
        rememberLazyListState(initialFirstVisibleItemIndex = getInitialScrollIndex(state, itemType))
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightDp = 50.dp

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { item ->
                state.selectedDuration.update {
                    updateDurationState(itemType, item, it)
                }
            }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            kotlin.runCatching {
                itemSpacer(textStyle, textModifier)
                items(listScrollCount) { index ->
                    Text(
                        text = items[index],
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = textStyle,
                        modifier = textModifier
                    )
                }
                itemSpacer(textStyle, textModifier)
            }
        }

        HorizontalDivider(
            modifier = Modifier.offset(y = itemHeightDp * visibleItemsMiddle),
            thickness = LocalDimen.current.extraSmall,
            color = dividerColor
        )

        HorizontalDivider(
            modifier = Modifier.offset(y = itemHeightDp * (visibleItemsMiddle + 1)),
            thickness = LocalDimen.current.extraSmall,
            color = dividerColor
        )
    }
}

fun getInitialScrollIndex(state: DurationPickerState, itemType: DefaultDurationType): Int =
    if (itemType == DefaultDurationType.HOURS) {
        state.selectedDuration.value.dropLast(3).ifNullOrEmpty { "0" }.toInt()
    } else state.selectedDuration.value.drop(3).ifNullOrEmpty { "0" }.toInt()

@Composable
fun rememberDurationPickerState(
    initialSelectedDuration: String? = null
) = remember { DurationPickerState(initialSelectedDuration) }

enum class DefaultDurationType {
    HOURS,
    MINUTE
}

class DurationPickerState(initialSelectedDuration: String?) {
    val hoursItems = MutableStateFlow((0..23).map { it.toString() })
    val minutesItems = MutableStateFlow((0..59).map { it.toString() })
    val selectedDuration = MutableStateFlow(initialSelectedDuration ?: "00:00")
}

private fun updateDurationState(
    itemType: DefaultDurationType,
    item: Int,
    it: String
) = runCatching {
    if (itemType == DefaultDurationType.HOURS) {
        String.format(
            "%02d:%02d",
            item,
            it.drop(3).ifNullOrEmpty { "0" }.toInt()
        )
    } else {
        String.format(
            "%02d:%02d",
            it.dropLast(3).ifNullOrEmpty { "0" }.toInt(),
            item
        )
    }
}.getOrElse { "00:00" }

private fun LazyListScope.itemSpacer(
    textStyle: TextStyle,
    modifier: Modifier
) {
    item {
        Text(
            text = "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = textStyle,
            modifier = modifier
        )
    }
}