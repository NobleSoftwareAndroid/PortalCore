package com.noblesoftware.portalcore.component.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.util.extension.dpToPx
import com.noblesoftware.portalcore.util.extension.ifNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBarMultiLine(
    modifier: Modifier,
    title: String,
    maxLines: Int = Int.MAX_VALUE,
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
    val localDensity = LocalDensity.current
    val titleHeightDp = remember { mutableStateOf(0.dp) }
    val titleWidthDp = remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .background(color = colorResource(id = R.color.neutral_solid_color))
            .fillMaxWidth(),
    ) {
        Box {
            MediumTopAppBar(
                modifier = modifier.background(color = Color.Yellow),
                collapsedHeight = topBarHeight,
                expandedHeight = topBarHeight.plus(titleHeightDp.value),
                title = {
                    Box(
                        modifier = Modifier
                            .onGloballyPositioned { coordinates ->
                                titleHeightDp.value =
                                    with(localDensity) { coordinates.size.height.toDp() }
                            }
                            .offset(
                                x = LocalDimen.current.extraRegular.plus(
                                    LocalDimen.current.regular
                                ),
                                y = -LocalDimen.current.extraRegular.plus(
                                    LocalDimen.current.regular
                                )
                            )
                    ) {
                        Text(
                            modifier = Modifier
                                .align(alignment = Alignment.Center),
                            text = title,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = maxLines,
                            color = colorResource(id = R.color.text_primary),
                            style = MaterialTheme.typography.labelLarge.copy(colorResource(id = R.color.text_primary))
                        )
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
                        IconButton(
                            modifier = Modifier,
                            onClick = {
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
                        IconButton(
                            modifier = Modifier,
                            onClick = {
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
                    Box(Modifier.onGloballyPositioned { coordinates ->
                        titleWidthDp.value =
                            with(localDensity) { coordinates.size.width.toDp() }
                    }) {
                        actions.invoke()
                    }
                }
            )
            Text(
                modifier = Modifier.padding(
                    start = LocalDimen.current.extraRegular.plus(LocalDimen.current.large)
                        .plus(0.5.dp),
                    top = LocalDimen.current.medium.plus(LocalDimen.current.extraSmall)
                        .plus(0.5.dp),
                    bottom = LocalDimen.current.medium,
                    end = LocalDimen.current.medium.plus(if (titleWidthDp.value.dpToPx() > 0) titleWidthDp.value else 0.dp),
                ),
                text = title,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(id = R.color.text_primary),
                style = MaterialTheme.typography.labelLarge.copy(colorResource(id = R.color.text_primary))
            )
        }
        tabs.let {
            tabs.invoke()
        }
        if (plain == false) {
            HorizontalDivider(thickness = 1.dp, color = colorResource(id = R.color.divider))
        }
    }
}