package com.noblesoftware.portalcorelibrary.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import com.noblesoftware.portalcore.component.compose.ButtonType
import com.noblesoftware.portalcore.component.compose.ButtonVariant
import com.noblesoftware.portalcore.component.compose.DefaultButton
import com.noblesoftware.portalcore.component.compose.DefaultSnackbar
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.DefaultTopAppBar
import com.noblesoftware.portalcore.component.compose.showDefaultSnackbar
import com.noblesoftware.portalcore.model.SnackbarState
import com.noblesoftware.portalcore.model.SnackbarType
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.extension.handleSafeScaffoldPadding

@Composable
fun SnackBarSampleScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
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
        modifier = Modifier.handleSafeScaffoldPadding(),
        topBar = {
            DefaultTopAppBar(
                modifier = Modifier,
                title = "Snackbar",
                canBack = true,
                navigator = navHostController
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { DefaultSnackbar(data = it, state = snackbarState.value) },
            )
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
                text = "Primary",
                buttonVariant = ButtonVariant.Primary
            ) {
                snackbarState.value = SnackbarState(
                    message = "Snackbar message",
                    type = SnackbarType.Primary,
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Neutral",
                buttonVariant = ButtonVariant.Neutral
            ) {
                snackbarState.value = SnackbarState(
                    message = "Snackbar message",
                    type = SnackbarType.Neutral,
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Danger",
                buttonVariant = ButtonVariant.Danger
            ) {
                snackbarState.value = SnackbarState(
                    message = "Snackbar message",
                    type = SnackbarType.Danger,
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Success",
                buttonVariant = ButtonVariant.Success
            ) {
                snackbarState.value = SnackbarState(
                    message = "Snackbar message",
                    type = SnackbarType.Success,
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Warning",
                buttonVariant = ButtonVariant.Warning
            ) {
                snackbarState.value = SnackbarState(
                    message = "Snackbar message",
                    type = SnackbarType.Warning,
                )
            }
            DefaultSpacer()
            HorizontalDivider()
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "isSuccess = true",
                buttonVariant = ButtonVariant.Success,
                buttonType = ButtonType.Outlined,
            ) {
                snackbarState.value = SnackbarState(
                    message = "Snackbar message",
                    isSuccess = true,
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "isSuccess = false",
                buttonVariant = ButtonVariant.Danger,
                buttonType = ButtonType.Outlined,
            ) {
                snackbarState.value = SnackbarState(
                    message = "Snackbar message",
                    isSuccess = false,
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "formatted error message",
                buttonVariant = ButtonVariant.Danger,
                buttonType = ButtonType.Outlined,
            ) {
                snackbarState.value = SnackbarState(
                    message = "failed to connect to backend.example.id/103.34.436.22 (port 432) after 5555 ms",
                    isSuccess = false,
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "formatted error timeout message",
                buttonVariant = ButtonVariant.Danger,
                buttonType = ButtonType.Outlined,
            ) {
                snackbarState.value = SnackbarState(
                    message = "timeout",
                    isSuccess = false,
                )
            }
            DefaultSpacer()
            HorizontalDivider()
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Multiple line",
                buttonVariant = ButtonVariant.Neutral,
                buttonType = ButtonType.Outlined,
            ) {
                snackbarState.value = SnackbarState(
                    message = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
                    isSuccess = true,
                )
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Two line",
                buttonVariant = ButtonVariant.Neutral,
                buttonType = ButtonType.Outlined,
            ) {
                snackbarState.value = SnackbarState(
                    message = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                    isSuccess = true,
                )
            }

        }
    }
}