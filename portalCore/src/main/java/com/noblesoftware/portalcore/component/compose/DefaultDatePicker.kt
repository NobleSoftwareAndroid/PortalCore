package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultDatePicker(
    datePickerState: DatePickerState,
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
        },
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}