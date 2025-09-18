package com.noblesoftware.portalcore.component.xml.duration_picker_bottom_sheet.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.xml.duration_picker_bottom_sheet.model.DurationPickerBottomSheetState
import com.noblesoftware.portalcore.component.xml.duration_picker_bottom_sheet.model.DurationPickerFormat
import com.noblesoftware.portalcore.theme.LocalDimen

@Composable
fun DurationPickerTitles(
    modifier: Modifier = Modifier,
    state: DurationPickerBottomSheetState,
    itemHeight: Int,
    @StringRes hourTitle: Int,
    @StringRes minuteTitle: Int,
    @StringRes secondTitle: Int
) {
    Row(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(start = itemHeight.dp.plus(LocalDimen.current.large))
                    .width(itemHeight.dp.plus(LocalDimen.current.extraRegular))
                    .height(itemHeight.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(hourTitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_primary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(start = itemHeight.dp.plus(LocalDimen.current.large))
                    .width(itemHeight.dp.plus(LocalDimen.current.extraRegular))
                    .height(itemHeight.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(minuteTitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_primary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (state.durationPickerFormat == DurationPickerFormat.H_M_S) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = itemHeight.dp.plus(LocalDimen.current.large))
                        .width(itemHeight.dp.plus(LocalDimen.current.extraRegular))
                        .height(itemHeight.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(secondTitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text_primary),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}