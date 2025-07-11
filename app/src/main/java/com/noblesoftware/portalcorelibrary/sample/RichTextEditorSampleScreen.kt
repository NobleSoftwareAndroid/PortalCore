package com.noblesoftware.portalcorelibrary.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.ButtonType
import com.noblesoftware.portalcore.component.compose.ButtonVariant
import com.noblesoftware.portalcore.component.compose.DefaultButton
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.DefaultTopAppBar
import com.noblesoftware.portalcore.component.compose.richeditor.RichEditorComposable
import com.noblesoftware.portalcore.component.compose.richeditor.component.DefaultFontBox
import com.noblesoftware.portalcore.component.xml.dynamic_bottom_sheet.DefaultDynamicBottomSheetDialog
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.extension.handleSafeScaffoldPadding
import com.noblesoftware.portalcore.util.extension.toHtmlFormatMention
import com.noblesoftware.portalcorelibrary.MainActivity

@Composable
fun RichTextEditorSampleScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val mention = remember { mutableStateOf("") }
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
            DefaultSpacer()
            Text("Richtext in bottomsheet")
            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Open Bottom Sheet",
                buttonType = ButtonType.Solid,
                buttonVariant = ButtonVariant.Neutral
            ) {
                DefaultDynamicBottomSheetDialog.showDialog(
                    fragmentManager = (context as MainActivity).supportFragmentManager,
                    tag = "tag",
                    buttonFirstEnable = true,
                    buttonSecondEnable = true,
                    isDismissible = true,
                    isInitialFullscreen = false,
                    buttonFirstText = R.string.close,
                    buttonSecondText = R.string.okay,
                ) {
                    RichEditorComposable(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = LocalDimen.current.regular),
                        value = "",
                        imageFormName = "image",
                        isImageEnabled = true,
                        isAntiCheatEnable = false,
                        onImageUpload = {},
                        onImageRetrieve = { "" },
                        onHtmlRetrieve = { mention.value },
                        onSnackbar = {},
                        onTextChanged = {},
                        onTextPaste = {},
                        onTextCopyOrCut = {},
                        action = {
                            Row {
                                DefaultFontBox(
                                    onClick = {
                                        mention.value =
                                            "<span style=\"color: #034aa6;\" contenteditable=\"false\"><strong>@${System.currentTimeMillis()}</strong></span>&nbsp;"
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_mention),
                                        tint = colorResource(id = R.color.text_primary),
                                        contentDescription = "",
                                    )
                                }
                                DefaultSpacer(width = LocalDimen.current.default)
                                DefaultFontBox(
                                    onClick = {
                                        mention.value = "agus_kurniawan".toHtmlFormatMention()
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_profile),
                                        tint = colorResource(id = R.color.text_primary),
                                        contentDescription = "",
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}