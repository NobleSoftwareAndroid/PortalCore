package com.noblesoftware.portalcorelibrary.sample

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.ButtonVariant
import com.noblesoftware.portalcore.component.compose.DefaultButton
import com.noblesoftware.portalcore.component.compose.DefaultSnackbar
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.DefaultTopAppBar
import com.noblesoftware.portalcore.component.compose.showDefaultSnackbar
import com.noblesoftware.portalcore.component.xml.dynamic_bottom_sheet.DefaultDynamicBottomSheetDialog
import com.noblesoftware.portalcore.component.xml.options_bottom_sheet.DefaultBottomSheetDialog
import com.noblesoftware.portalcore.enums.BottomSheetActionType
import com.noblesoftware.portalcore.enums.BottomSheetType
import com.noblesoftware.portalcore.model.SelectOption
import com.noblesoftware.portalcore.model.SnackbarState
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.extension.findActivity

@Composable
fun BottomSheetSampleScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val snackbarState = remember {
        mutableStateOf(
            SnackbarState(
                message = null,
                isSuccess = false
            )
        )
    }
    LaunchedEffect(snackbarState.value) {
        if (!snackbarState.value.message.isNullOrBlank() || snackbarState.value.messageId != null) {
            snackbarHostState.showDefaultSnackbar(
                context = context,
                snackbar = snackbarState.value,
                actionLabel = "OK",
            ).apply {
                when (this) {
                    SnackbarResult.Dismissed -> {
                        snackbarState.value =
                            snackbarState.value.copy(message = null, messageId = null)
                    }

                    SnackbarResult.ActionPerformed -> {
                        snackbarState.value =
                            snackbarState.value.copy(message = null, messageId = null)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                modifier = Modifier,
                title = "Bottom Sheet",
                canBack = true,
                navigator = navHostController
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState,
                snackbar = { DefaultSnackbar(data = it, state = snackbarState.value) })
        }
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
                text = "Option Bottom Sheet",
                buttonVariant = ButtonVariant.Neutral
            ) {
                DefaultBottomSheetDialog.showDialog(
                    fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
                    bottomSheetType = BottomSheetType.SINGLE_SELECTION,
                    options = listOf(
                        SelectOption(1, false, "Sample Option 1"),
                        SelectOption(2, false, "Sample Option 2"),
                        SelectOption(3, false, "Sample Option 3"),
                        SelectOption(4, false, "Sample Option 4"),
                        SelectOption(5, false, "Sample Option 5")
                    ),
                    onSelected = { options ->
                        options.firstOrNull()?.let { option ->
                            snackbarState.value = SnackbarState(
                                message = "Selected ${option.name}",
                                isSuccess = true,
                            )
                        }
                    },
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Option Bottom Sheet (Empty)",
                buttonVariant = ButtonVariant.Neutral
            ) {
                DefaultBottomSheetDialog.showDialog(
                    fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
                    bottomSheetType = BottomSheetType.SINGLE_SELECTION,
                    options = listOf(),
                    onSelected = { options ->
                        options.firstOrNull()?.let { option ->
                            snackbarState.value = SnackbarState(
                                message = "Selected ${option.name}",
                                isSuccess = true,
                            )
                        }
                    },
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Option (Search) Bottom Sheet",
                buttonVariant = ButtonVariant.Neutral
            ) {
                DefaultBottomSheetDialog.showDialog(
                    fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
                    bottomSheetType = BottomSheetType.SINGLE_SELECTION_WITH_SEARCH,
                    searchHint = "Search",
                    options = listOf(
                        SelectOption(1, false, "Sample Option 1"),
                        SelectOption(2, false, "Sample Option 2"),
                        SelectOption(3, false, "Sample Option 3"),
                        SelectOption(4, false, "Sample Option 4"),
                        SelectOption(5, false, "Sample Option 5")
                    ),
                    onSelected = { options ->
                        options.firstOrNull()?.let { option ->
                            snackbarState.value = SnackbarState(
                                message = "Selected ${option.name}",
                                isSuccess = true,
                            )
                        }
                    }
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Dynamic Bottom Sheet",
                buttonVariant = ButtonVariant.Neutral
            ) {
                DefaultDynamicBottomSheetDialog.showDialog(
                    fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
                    buttonFirstText = R.string.close,
                    buttonFirstType = BottomSheetActionType.NEUTRAL,
                    buttonFirstOnClick = {

                    },
                    content = {
                        Column {
                            Text(text = "This is compose component")
                        }
                    }
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Dynamic Bottom Sheet No Button",
                buttonVariant = ButtonVariant.Neutral
            ) {
                DefaultDynamicBottomSheetDialog.showDialog(
                    fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
                    buttonFirstEnable = false,
                    buttonSecondEnable = false,
                    content = {
                        Column {
                            Text(text = "This is compose component")
                        }
                    }
                )
            }
            DefaultSpacer()
        }
    }
}