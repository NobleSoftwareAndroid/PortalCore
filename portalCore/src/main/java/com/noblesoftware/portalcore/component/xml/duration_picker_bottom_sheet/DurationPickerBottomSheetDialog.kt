package com.noblesoftware.portalcore.component.xml.duration_picker_bottom_sheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.ButtonSize
import com.noblesoftware.portalcore.component.compose.ButtonType
import com.noblesoftware.portalcore.component.compose.ButtonVariant
import com.noblesoftware.portalcore.component.compose.DefaultButton
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.databinding.BottomSheetDialogBinding
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.PortalCoreTheme
import com.noblesoftware.portalcore.util.extension.fadingEdge
import com.noblesoftware.portalcore.util.extension.isTrue
import com.noblesoftware.portalcore.util.extension.loge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class DurationPickerBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDialogBinding

    @StringRes
    private var title: Int = R.string.empty_string

    /** if [isStatusBarTransparent] = true -> set this activity and PortalCoreTheme statusBar to transparent */
    private var isStatusBarTransparent: Boolean = false
    private var onDismiss: () -> Unit = {}

    override fun getTheme(): Int = R.style.ModalBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.bottom_sheet_dialog,
                container,
                false
            )
        binding.lifecycleOwner = viewLifecycleOwner
        val view = binding.root

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val totalItems = 120
                val visibleItems = 7
                val itemHeight = 50
                val fadingEdgeGradient = remember {
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.5f to Color.Black,
                        1f to Color.Transparent
                    )
                }

                val listStateHours =
                    rememberLazyListState(initialFirstVisibleItemIndex = (totalItems / 2) - (visibleItems / 2))
                val flingBehaviorHours = rememberSnapFlingBehavior(lazyListState = listStateHours)
                LaunchedEffect(listStateHours.isScrollInProgress) {
                    if (!listStateHours.isScrollInProgress) {
                        val visible = listStateHours.layoutInfo.visibleItemsInfo
                        val chosen =
                            runCatching { visible[visibleItems / 2].index % 60 }.getOrElse { 0 }
                        loge("Chosen: $chosen")
                    }
                }

                val listStateMinutes =
                    rememberLazyListState(initialFirstVisibleItemIndex = (totalItems / 2) - (visibleItems / 2))
                val flingBehaviorMinutes =
                    rememberSnapFlingBehavior(lazyListState = listStateMinutes)
                LaunchedEffect(listStateMinutes.isScrollInProgress) {
                    if (!listStateMinutes.isScrollInProgress) {
                        val visible = listStateHours.layoutInfo.visibleItemsInfo
                        val chosen =
                            runCatching { visible[visibleItems / 2].index % 60 }.getOrElse { 0 }
                        loge("Chosen: $chosen")
                    }
                }

                val listStateSeconds =
                    rememberLazyListState(initialFirstVisibleItemIndex = (totalItems / 2) - (visibleItems / 2))
                val flingBehaviorSeconds =
                    rememberSnapFlingBehavior(lazyListState = listStateSeconds)
                LaunchedEffect(listStateSeconds.isScrollInProgress) {
                    if (!listStateHours.isScrollInProgress) {
                        val visible = listStateHours.layoutInfo.visibleItemsInfo
                        val chosen =
                            runCatching { visible[visibleItems / 2].index % 60 }.getOrElse { 0 }
                        loge("Chosen: $chosen")
                    }
                }

                PortalCoreTheme(
                    window = requireActivity().window,
                    isStatusBarTransparent = isStatusBarTransparent
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .nestedScroll(rememberNestedScrollInteropConnection()),
                        color = colorResource(id = R.color.background_body)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            /** Title */
                            DefaultSpacer()
                            Text(
                                modifier = Modifier.padding(start = LocalDimen.current.regular),
                                text = "Time picker",
                                style = MaterialTheme.typography.labelLarge,
                                color = colorResource(R.color.text_primary)
                            )
                            DefaultSpacer(height = LocalDimen.current.extraRegular)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((visibleItems * itemHeight).dp),
                            ) {
                                /** Titles */
                                Row(modifier = Modifier.fillMaxSize()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(start = itemHeight.dp.plus(LocalDimen.current.large))
                                                .width(itemHeight.dp.plus(LocalDimen.current.extraRegular))
                                                .height(itemHeight.dp),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Text(
                                                modifier = Modifier,
                                                text = "hours",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = colorResource(R.color.text_primary),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(start = itemHeight.dp.plus(LocalDimen.current.large))
                                                .width(itemHeight.dp.plus(LocalDimen.current.extraRegular))
                                                .height(itemHeight.dp),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Text(
                                                modifier = Modifier,
                                                text = "min",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = colorResource(R.color.text_primary),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(start = itemHeight.dp.plus(LocalDimen.current.large))
                                                .width(itemHeight.dp.plus(LocalDimen.current.extraRegular))
                                                .height(itemHeight.dp),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Text(
                                                modifier = Modifier,
                                                text = "sec",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = colorResource(R.color.text_primary),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }

                                /** LazyColumn */
                                Row(modifier = Modifier.fillMaxSize()) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .fadingEdge(fadingEdgeGradient),
                                        state = listStateHours,
                                        flingBehavior = flingBehaviorHours,
                                        contentPadding = PaddingValues(end = LocalDimen.current.large)
                                    ) {
                                        items(count = totalItems) { index ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(itemHeight.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = (index % 60).toString(),
                                                    style = MaterialTheme.typography.labelLarge.copy(
                                                        fontSize = 20.sp
                                                    ),
                                                    color = colorResource(R.color.text_primary)
                                                )
                                            }
                                        }
                                    }
                                    LazyColumn(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .fadingEdge(fadingEdgeGradient),
                                        state = listStateMinutes,
                                        flingBehavior = flingBehaviorMinutes,
                                        contentPadding = PaddingValues(end = LocalDimen.current.large)
                                    ) {
                                        items(count = totalItems) { index ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(itemHeight.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = (index % 60).toString(),
                                                    style = MaterialTheme.typography.labelLarge.copy(
                                                        fontSize = 20.sp
                                                    ),
                                                    color = colorResource(R.color.text_primary)
                                                )
                                            }
                                        }
                                    }
                                    LazyColumn(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .fadingEdge(fadingEdgeGradient),
                                        state = listStateSeconds,
                                        flingBehavior = flingBehaviorSeconds,
                                        contentPadding = PaddingValues(end = LocalDimen.current.large)
                                    ) {
                                        items(count = totalItems) { index ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(itemHeight.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = (index % 60).toString(),
                                                    style = MaterialTheme.typography.labelLarge.copy(
                                                        fontSize = 20.sp
                                                    ),
                                                    color = colorResource(R.color.text_primary)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            /** Buttons */
                            DefaultSpacer(height = LocalDimen.current.extraRegular)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = LocalDimen.current.regular),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                DefaultButton(
                                    modifier = Modifier.weight(1f),
                                    buttonSize = ButtonSize.Medium,
                                    buttonType = ButtonType.Outlined,
                                    buttonVariant = ButtonVariant.Neutral,
                                    text = stringResource(R.string.cancel),
                                ) {
                                }
                                DefaultSpacer()
                                DefaultButton(
                                    modifier = Modifier.weight(1f),
                                    buttonSize = ButtonSize.Medium,
                                    buttonType = ButtonType.Solid,
                                    buttonVariant = ButtonVariant.Primary,
                                    text = "Set",
                                ) {
                                }
                            }
                            DefaultSpacer(height = LocalDimen.current.extraRegular)
                        }
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isStatusBarTransparent.isTrue()) {
            val window = requireActivity().window
            window.statusBarColor = Color.Transparent.toArgb()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss.invoke()
    }

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            @StringRes title: Int = R.string.empty_string,
            isStatusBarTransparent: Boolean = false,
            onDismiss: () -> Unit = {},
        ) {
            DurationPickerBottomSheetDialog().apply {
                this.title = title
                this.isStatusBarTransparent = isStatusBarTransparent
                this.onDismiss = onDismiss
            }.show(fragmentManager, DurationPickerBottomSheetDialog::class.java.simpleName)
        }
    }

}
