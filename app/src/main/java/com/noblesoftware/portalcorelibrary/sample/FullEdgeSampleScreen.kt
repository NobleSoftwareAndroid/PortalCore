package com.noblesoftware.portalcorelibrary.sample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.ButtonVariant
import com.noblesoftware.portalcore.component.compose.DefaultButton
import com.noblesoftware.portalcore.component.compose.DefaultSnackbar
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.showDefaultSnackbar
import com.noblesoftware.portalcore.component.xml.dynamic_bottom_sheet.DefaultDynamicBottomSheetDialog
import com.noblesoftware.portalcore.component.xml.options_bottom_sheet.DefaultBottomSheetDialog
import com.noblesoftware.portalcore.enums.BottomSheetActionType
import com.noblesoftware.portalcore.enums.BottomSheetType
import com.noblesoftware.portalcore.model.SelectOption
import com.noblesoftware.portalcore.model.SnackbarState
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes
import com.noblesoftware.portalcore.util.extension.findActivity
import com.noblesoftware.portalcore.util.extension.setTransparentStatusBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FullEdgeSampleScreen(
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

    val view = LocalView.current
    LaunchedEffect(true) {
        view.setTransparentStatusBar(transparentStatusBar = true)
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
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { DefaultSnackbar(data = it, state = snackbarState.value) },
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background_body))
                .navigationBarsPadding()
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        /** Background Image */
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            painter = painterResource(id = R.drawable.img_portal_kampus_logo),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = LocalDimen.current.regular),
                        ) {
                            DefaultSpacer(height = LocalDimen.current.extraLarge)
                            /** Logo */
                            Box(
                                modifier = Modifier
                                    .size(86.dp)
                                    .clip(LocalShapes.small)
                                    .background(
                                        colorResource(id = com.noblesoftware.portalcore.R.color.background_body)
                                    )
                                    .padding(LocalDimen.current.medium)
                                    .align(Alignment.CenterHorizontally),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_portal_kampus_logo),
                                    contentDescription = ""
                                )
                            }
                            DefaultSpacer()
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.Green),
                    )
                    DefaultSpacer()
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LocalDimen.current.regular)
                    ) {
                        DefaultButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Full Edge Bottom Sheet",
                            buttonVariant = ButtonVariant.Neutral
                        ) {
                            DefaultBottomSheetDialog.showDialog(
                                fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
                                bottomSheetType = BottomSheetType.SINGLE_SELECTION,
                                isStatusBarTransparent = true,
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
                            text = "Option (Search) Full Edge Bottom Sheet",
                            buttonVariant = ButtonVariant.Neutral
                        ) {
                            DefaultBottomSheetDialog.showDialog(
                                fragmentManager = (activity as AppCompatActivity).supportFragmentManager,
                                bottomSheetType = BottomSheetType.SINGLE_SELECTION_WITH_SEARCH,
                                isStatusBarTransparent = true,
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
                                isStatusBarTransparent = true,
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
                        DefaultSpacer(height = 1000.dp)
                    }
                }
            }

        }
    }
}