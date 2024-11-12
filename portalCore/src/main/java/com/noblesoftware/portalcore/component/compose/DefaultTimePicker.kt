package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTimePicker(
    timePickerState: TimePickerState,
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
                Text(
                    text = confirmText,
                    color = colorResource(id = R.color.primary_solid_bg)
                )
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.padding(bottom = LocalDimen.current.default),
                onClick = {
                    onDismiss.invoke()
                }) {
                Text(
                    text = dismissText,
                    color = colorResource(id = R.color.primary_solid_bg)
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalDimen.current.regular),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = LocalDimen.current.default)
                    .align(Alignment.Start),
                text = stringResource(id = R.string.select_time),
                style = MaterialTheme.typography.labelLarge.copy(
                    colorResource(id = R.color.text_secondary)
                )
            )
            DefaultSpacer(height = LocalDimen.current.extraRegular)
            TimePicker(
                state = timePickerState,
            )
        }
    }
}