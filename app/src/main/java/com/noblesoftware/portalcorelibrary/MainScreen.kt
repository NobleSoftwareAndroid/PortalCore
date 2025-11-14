package com.noblesoftware.portalcorelibrary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavHostController
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.ButtonVariant
import com.noblesoftware.portalcore.component.compose.DefaultButton
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.DefaultTopAppBar
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.extension.handleSafeScaffoldPadding
import com.noblesoftware.portalcore.util.extension.setTransparentStatusBar

@Composable
fun MainScreen(
    navHostController: NavHostController
) {
    val text = remember { mutableStateOf("") }
    val view = LocalView.current
    LaunchedEffect(true) {
        view.setTransparentStatusBar(transparentStatusBar = false)
    }
    Scaffold(
        modifier = Modifier.handleSafeScaffoldPadding(),
        topBar = {
            DefaultTopAppBar(
                modifier = Modifier,
                title = "Portal Core Example",
                navigator = navHostController
            )
        },
    ) {
        Column(
            modifier = Modifier
                .background(color = colorResource(id = R.color.background_body))
                .fillMaxSize()
                .padding(it)
                .padding(
                    horizontal = LocalDimen.current.regular,
                    vertical = LocalDimen.current.extraLarge
                )
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text.value,
                onValueChange = { text.value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrect = false
                ),
                visualTransformation = VisualTransformation.None
            )
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Common Components",
                buttonVariant = ButtonVariant.Neutral
            ) {
                navHostController.navigate(route = CommonRoute)
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Snackbar",
                buttonVariant = ButtonVariant.Neutral
            ) {
                navHostController.navigate(route = SnackBarRoute)
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Bottom Sheet",
                buttonVariant = ButtonVariant.Neutral
            ) {
                navHostController.navigate(route = BottomSheetRoute)
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Container",
                buttonVariant = ButtonVariant.Neutral
            ) {
                navHostController.navigate(route = ContainerRoute)
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Full Edge",
                buttonVariant = ButtonVariant.Neutral
            ) {
                navHostController.navigate(route = FullEdgeRoute)
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Dialog",
                buttonVariant = ButtonVariant.Neutral
            ) {
                navHostController.navigate(route = DialogRoute)
            }
            DefaultSpacer()
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "RichText Editor",
                buttonVariant = ButtonVariant.Neutral
            ) {
                navHostController.navigate(route = RichTextEditorRoute)
            }
        }
    }
}