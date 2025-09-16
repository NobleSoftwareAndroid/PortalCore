package com.noblesoftware.portalcorelibrary.sample

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.ButtonVariant
import com.noblesoftware.portalcore.component.compose.DefaultButton
import com.noblesoftware.portalcore.component.compose.DefaultDatePicker
import com.noblesoftware.portalcore.component.compose.DefaultDialog
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.DefaultTopAppBar
import com.noblesoftware.portalcore.component.compose.DialogType
import com.noblesoftware.portalcore.component.compose.TextLabel
import com.noblesoftware.portalcore.component.compose.TopAppBarTitle
import com.noblesoftware.portalcore.component.xml.dynamic_dialog.DefaultDynamicDialog
import com.noblesoftware.portalcore.component.xml.dynamic_dialog.DefaultDynamicDialog.Companion.dismissDialog
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes
import com.noblesoftware.portalcore.util.DateTimeHelper
import com.noblesoftware.portalcore.util.extension.findActivity
import com.noblesoftware.portalcore.util.extension.handleSafeScaffoldPadding
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSampleScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    val showDefaultDialog = remember {
        mutableStateOf(false)
    }

    val showDefaultIconLargeDialog = remember {
        mutableStateOf(false)
    }

    val showAlertDialog = remember {
        mutableStateOf(false)
    }

    val showDatePicker = remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.handleSafeScaffoldPadding(),
        topBar = {
            DefaultTopAppBar(
                modifier = Modifier,
                canBack = true,
                navigator = navHostController,
                titleComposable = {
                    Box(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.top_bar_height))
                    ) {
                        Row(
                            modifier = Modifier.align(alignment = Alignment.Center),
                        ) {
                            TopAppBarTitle(
                                title = "Dialog"
                            )
                            DefaultSpacer(width = LocalDimen.current.default)
                            TextLabel(label = "Example")
                        }
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.background_body))
                .padding(paddingValues = it)
                .verticalScroll(rememberScrollState())
                .then(
                    Modifier.padding(LocalDimen.current.regular)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Default Dialog",
                buttonVariant = ButtonVariant.Neutral
            ) {
                showDefaultDialog.value = true
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Default Dialog (Icon Large)",
                buttonVariant = ButtonVariant.Neutral
            ) {
                showDefaultIconLargeDialog.value = true
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Alert Dialog",
                buttonVariant = ButtonVariant.Danger
            ) {
                showAlertDialog.value = true
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Date Picker",
                buttonVariant = ButtonVariant.Primary
            ) {
                showDatePicker.value = true
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Dynamic Dialog",
                buttonVariant = ButtonVariant.Neutral
            ) {
                DefaultDynamicDialog.showDialog(
                    fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
                    onDismiss = {},
                    content = {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(LocalDimen.current.regular)
                                .shadow(LocalDimen.current.small),
                            shape = LocalShapes.medium,
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = com.noblesoftware.portalcore.R.color.background_body))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(LocalDimen.current.regular)
                            ) {
                                DefaultButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Dismiss from outside",
                                    buttonVariant = ButtonVariant.Primary
                                ) {
                                    activity.dismissDialog()
                                }
                                DefaultSpacer()
                                Text(text = "This is compose component")
                            }
                        }
                    }
                )
            }
        }

        // Logout Dialog
        if (showAlertDialog.value) {
            DefaultDialog(
                title = "Are you sure?",
                dialogType = DialogType.Alert,
                positiveButtonText = "Logout",
                negativeButtonText = stringResource(id = R.string.cancel),
                onPositive = {
                    showAlertDialog.value = false
                },
                onNegative = {
                    showAlertDialog.value = false
                    null
                },
                onDismissRequest = {
                    showAlertDialog.value = false
                    null
                },
//                dismissOnBackPress = false,
//                dismissOnClickOutside = false
            )
        }

        if (showDefaultDialog.value) {
            DefaultDialog(
                title = "Default Dialog",
                message = "Default Dialog Message",
                icon = painterResource(id = R.drawable.img_shield),
                dialogType = DialogType.Default,
                positiveButtonText = "Okay",
                negativeButtonText = stringResource(id = R.string.cancel),
                onPositive = {
                    showDefaultDialog.value = false
                },
                onNegative = {
                    showDefaultDialog.value = false
                    null
                },
                onDismissRequest = {
                    showDefaultDialog.value = false
                    null
                },
            )
        }

        if (showDefaultIconLargeDialog.value) {
            DefaultDialog(
                title = "Default Dialog (Icon Large)",
                message = "Default Dialog Message (Icon Large)",
                icon = painterResource(id = R.drawable.img_shield),
                iconSize = 160.dp,
                dialogType = DialogType.Default,
                positiveButtonText = "Okay",
                negativeButtonText = stringResource(id = R.string.cancel),
                onPositive = {
                    showDefaultIconLargeDialog.value = false
                },
                onNegative = {
                    showDefaultIconLargeDialog.value = false
                    null
                },
                onDismissRequest = {
                    showDefaultIconLargeDialog.value = false
                    null
                },
            )
        }

        // Date Picker
        if (showDatePicker.value) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = System.currentTimeMillis()
            )
            DefaultDatePicker(
                datePickerState = datePickerState,
                confirmText = "OK",
                dismissText = "Cancel",
                onConfirm = {
                    val date =
                        datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                    val formatDate =
                        DateTimeHelper.format(
                            Date(date),
                            DateTimeHelper.FORMAT_DATE_DMY
                        )

                    showDatePicker.value = false
                },
                onDismiss = {
                    showDatePicker.value = false
                },
            )
        }

    }
}