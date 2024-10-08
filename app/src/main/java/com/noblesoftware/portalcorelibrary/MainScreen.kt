package com.noblesoftware.portalcorelibrary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.noblesoftware.portalcore.component.compose.ButtonVariant
import com.noblesoftware.portalcore.component.compose.DefaultButton
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.theme.LocalDimen

@Composable
fun MainScreen(
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .background(color = colorResource(id = com.noblesoftware.portalcore.R.color.background_body))
            .fillMaxSize()
            .padding(
                horizontal = LocalDimen.current.regular,
                vertical = LocalDimen.current.extraLarge
            )
    ) {
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
    }
}