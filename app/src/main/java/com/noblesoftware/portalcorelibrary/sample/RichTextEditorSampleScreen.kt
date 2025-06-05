package com.noblesoftware.portalcorelibrary.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.DefaultTopAppBar
import com.noblesoftware.portalcore.component.compose.richeditor.RichEditorComposable
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.extension.handleSafeScaffoldPadding

@Composable
fun RichTextEditorSampleScreen(
    navHostController: NavHostController
) {
    Scaffold(
        modifier = Modifier.handleSafeScaffoldPadding(),
        topBar = {
            DefaultTopAppBar(
                modifier = Modifier,
                title = "RichText Editor",
                canBack = true,
                navigator = navHostController
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
        ) {
            Text("Default Editor")
            RichEditorComposable(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                imageFormName = "image",
                isImageEnabled = false,
                isAntiCheatEnable = false,
                onImageUpload = {},
                onImageRetrieve = { "" },
                onSnackbar = {},
                onTextChanged = {},
                onTextPaste = {},
                onTextCopyOrCut = {}
            )
            DefaultSpacer()
            Text("Anticheat enable Editor")
            RichEditorComposable(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                imageFormName = "image",
                isImageEnabled = false,
                isAntiCheatEnable = true,
                onImageUpload = {},
                onImageRetrieve = { "" },
                onSnackbar = {},
                onTextChanged = {},
                onTextPaste = {},
                onTextCopyOrCut = {}
            )
        }
    }
}