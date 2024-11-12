package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTimePicker(
    timePickerState: TimePickerState,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        ),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalDimen.current.regular)
                .shadow(LocalDimen.current.small),
            shape = LocalShapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.background_body))
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
                        colorResource(id = R.color.text_primary)
                    )
                )
                DefaultSpacer(height = LocalDimen.current.extraRegular)
                TimePicker(
                    state = timePickerState,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onDismiss.invoke()
                        },
                    ) {
                        Text(
                            text = dismissText,
                            color = colorResource(id = R.color.primary_solid_bg)
                        )
                    }
                    DefaultSpacer(width = LocalDimen.current.default)
                    TextButton(
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
                }
            }
        }
    }
}