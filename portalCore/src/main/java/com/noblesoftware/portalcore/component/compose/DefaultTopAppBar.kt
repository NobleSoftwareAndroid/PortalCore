package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.PortalCoreTheme

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DefaultTopAppBarPreview() {
    PortalCoreTheme {
        DefaultTopAppBar(modifier = Modifier, title = "App Bar")
    }
}

/**
 * The DefaultTopAppBar composable is used to create a top app bar (also known as a toolbar) with customizable properties. It provides consistent navigation and action controls aligned with Material Design principles.
 *
 * Parameters
 * @param modifier A modifier that can be used to customize the appearance of the top app bar.
 * @param title The title text to display in the app bar.
 * @param plain An optional boolean indicating whether the app bar should have a plain appearance (without elevation or background). Defaults to false.
 * @param canBack An optional boolean indicating whether a back navigation button should be displayed. Defaults to false.
 * @param canClose An optional boolean indicating whether a close button should be displayed. Defaults to false.
 * @param onBackClick A lambda function to execute when the back button is clicked.
 * @param onCloseClick A lambda function to execute when the close button is clicked.
 * @param navigator An optional NavHostController for handling navigation actions.
 * @param actions An optional composable lambda that defines additional action icons or buttons to display in the app bar.
 *
 * @sample com.noblesoftware.portalcore.component.compose.ExampleDefaultTopAppBar
 *
 * @author VPN Android Team
 * @since 2024
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    modifier: Modifier,
    title: String? = stringResource(id = R.string.empty_string),
    titleComposable: @Composable (() -> Unit)? = null,
    plain: Boolean? = false,
    canBack: Boolean? = false,
    canClose: Boolean? = false,
    onBackClick: () -> Any? = {},
    onCloseClick: () -> Any? = {},
    navigator: NavHostController? = null,
    actions: @Composable () -> Unit? = {},
    tabs: @Composable () -> Unit? = {},
) {
    val topBarHeight = dimensionResource(id = R.dimen.top_bar_height)
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopAppBar(
            modifier = modifier
                .height(topBarHeight),
            title = {
                if (titleComposable != null) {
                    titleComposable.invoke()
                } else {
                    title?.let {
                        Box(
                            modifier = Modifier
                                .height(topBarHeight)
                        ) {
                            TopAppBarTitle(
                                modifier = Modifier.Companion.align(alignment = Alignment.Center),
                                title = title
                            )
                        }
                    }
                }
            },
            colors = TopAppBarColors(
                containerColor = colorResource(id = R.color.neutral_solid_color),
                scrolledContainerColor = colorResource(id = R.color.neutral_solid_color),
                titleContentColor = colorResource(id = R.color.text_primary),
                navigationIconContentColor = colorResource(id = R.color.text_secondary),
                actionIconContentColor = colorResource(id = R.color.text_secondary)
            ),
            navigationIcon = {
                if (canBack == true) {
                    IconButton(onClick = {
                        if (navigator != null) {
                            navigator.navigateUp()
                        } else {
                            onBackClick.invoke()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back),
                            tint = colorResource(id = R.color.text_secondary),
                        )
                    }
                } else if (canClose == true) {
                    IconButton(onClick = {
                        if (navigator != null) {
                            navigator.navigateUp()
                        } else {
                            onCloseClick.invoke()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = stringResource(R.string.close),
                            tint = colorResource(id = R.color.text_secondary),
                        )
                    }
                }
            },
            actions = {
                actions.invoke()
            }
        )
        tabs.let {
            tabs.invoke()
        }
        if (plain == false) {
            HorizontalDivider(thickness = 1.dp, color = colorResource(id = R.color.divider))
        }
    }
}

@Composable
fun TopAppBarTitle(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        color = colorResource(id = R.color.text_primary),
        style = MaterialTheme.typography.labelLarge.copy(colorResource(id = R.color.text_primary))
    )
}


@Composable
private fun ExampleDefaultTopAppBar() {
    DefaultTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = "Sample Plain Top Bar",
        plain = true,
        canBack = true,
        actions = {
            Row {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_help),
                        contentDescription = "Help",
                        tint = colorResource(id = R.color.text_secondary)
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = "User",
                        tint = colorResource(id = R.color.text_secondary)
                    )
                }
            }
        }
    )
}