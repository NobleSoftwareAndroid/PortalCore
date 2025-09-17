package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen

/**
 * The DefaultProgressDialog composable is used to display a circular progress indicator (loading spinner) within a dialog. It’s commonly used to indicate that an operation is in progress or data is being fetched.
 *
 * Parameters
 * @param show A boolean value indicating whether the progress dialog should be displayed.
 *
 * @sample com.noblesoftware.portalcore.component.compose.ExampleProgressDialog
 * 
 * @author VPN Android Team
 * @since 2024
 **/

@Composable
fun DefaultProgressDialog(
    show: Boolean
) {
    var showDialog by remember { mutableStateOf(false) }
    showDialog = show

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(LocalDimen.current.extraLarge),
                    color = colorResource(id = R.color.primary_solid_bg),
                    trackColor = colorResource(id = R.color.primary_outlined_hover_bg),
                )
            }
        }
    }
}

@Composable
private fun ExampleProgressDialog(showProgress: MutableState<Boolean>) {
    DefaultProgressDialog(show = showProgress.value)
}