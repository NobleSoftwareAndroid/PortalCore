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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
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
import com.noblesoftware.portalcore.component.xml.duration_picker_bottom_sheet.component.DurationPickerTitles
import com.noblesoftware.portalcore.component.xml.duration_picker_bottom_sheet.model.DurationPickerBottomSheetResult
import com.noblesoftware.portalcore.component.xml.duration_picker_bottom_sheet.model.DurationPickerBottomSheetState
import com.noblesoftware.portalcore.component.xml.duration_picker_bottom_sheet.model.DurationPickerFormat
import com.noblesoftware.portalcore.databinding.BottomSheetDialogBinding
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.PortalCoreTheme
import com.noblesoftware.portalcore.util.extension.fadingEdge
import com.noblesoftware.portalcore.util.extension.isTrue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class DurationPickerBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDialogBinding

    @StringRes
    private var title: Int = R.string.edit_timer

    @StringRes
    private var hourTitle: Int = R.string.hours

    @StringRes
    private var minuteTitle: Int = R.string.min

    @StringRes
    private var secondTitle: Int = R.string.sec

    @StringRes
    private var negativeButtonText: Int = R.string.cancel

    @StringRes
    private var positiveButtonText: Int = R.string.set

    /** if [isStatusBarTransparent] = true -> set this activity and PortalCoreTheme statusBar to transparent */
    private var isStatusBarTransparent: Boolean = false
    private var durationPickerBottomSheetState: DurationPickerBottomSheetState =
        DurationPickerBottomSheetState()
    private var onDismiss: () -> Unit = {}
    private var onNegative: () -> Unit = {}
    private var onPositive: (result: DurationPickerBottomSheetResult) -> Unit = {}

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
                val state = remember { durationPickerBottomSheetState }
                val result = remember {
                    mutableStateOf(
                        DurationPickerBottomSheetResult(
                            hour = state.initialHour,
                            minute = state.initialMinute,
                            second = state.initialSecond
                        )
                    )
                }
                val itemHeight = 50
                val loops = 100
                val fadingEdgeGradient = remember {
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.5f to Color.Black,
                        1f to Color.Transparent
                    )
                }

                /** Hours State */
                val hourItems = state.hourLimit * loops
                val listStateHours =
                    rememberLazyListState(initialFirstVisibleItemIndex = (hourItems / 2) - (state.visibleItems / 2) + state.initialHour)
                val flingBehaviorHours = rememberSnapFlingBehavior(lazyListState = listStateHours)
                LaunchedEffect(listStateHours.isScrollInProgress) {
                    if (!listStateHours.isScrollInProgress) {
                        val visible = listStateHours.layoutInfo.visibleItemsInfo
                        val chosen =
                            runCatching { visible[state.visibleItems / 2].index % state.hourLimit }.getOrElse { 0 }
                        result.value = result.value.copy(hour = chosen)
                    }
                }

                /** Minutes State */
                val minuteItems = state.minuteLimit * loops
                val listStateMinutes =
                    rememberLazyListState(initialFirstVisibleItemIndex = (minuteItems / 2) - (state.visibleItems / 2) + state.initialMinute)
                val flingBehaviorMinutes =
                    rememberSnapFlingBehavior(lazyListState = listStateMinutes)
                LaunchedEffect(listStateMinutes.isScrollInProgress) {
                    if (!listStateMinutes.isScrollInProgress) {
                        val visible = listStateMinutes.layoutInfo.visibleItemsInfo
                        val chosen =
                            runCatching { visible[state.visibleItems / 2].index % state.minuteLimit }.getOrElse { 0 }
                        result.value = result.value.copy(minute = chosen)
                    }
                }

                /** Seconds State */
                val secondItems = state.secondLimit * loops
                val listStateSeconds =
                    rememberLazyListState(initialFirstVisibleItemIndex = (secondItems / 2) - (state.visibleItems / 2) + state.initialSecond)
                val flingBehaviorSeconds =
                    rememberSnapFlingBehavior(lazyListState = listStateSeconds)

                /** Add Conditional for Second's LaunchedEffect */
                if (state.durationPickerFormat == DurationPickerFormat.H_M_S) {
                    LaunchedEffect(listStateSeconds.isScrollInProgress) {
                        if (!listStateHours.isScrollInProgress) {
                            val visible = listStateSeconds.layoutInfo.visibleItemsInfo
                            val chosen =
                                runCatching { visible[state.visibleItems / 2].index % state.secondLimit }.getOrElse { 0 }
                            result.value = result.value.copy(second = chosen)
                        }
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
                                text = stringResource(title),
                                style = MaterialTheme.typography.labelLarge,
                                color = colorResource(R.color.text_primary)
                            )
                            DefaultSpacer(height = LocalDimen.current.extraRegular)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((state.visibleItems * itemHeight).dp),
                            ) {
                                /** Time Titles */
                                DurationPickerTitles(
                                    state = state,
                                    itemHeight = itemHeight,
                                    hourTitle = hourTitle,
                                    minuteTitle = minuteTitle,
                                    secondTitle = secondTitle
                                )

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
                                        items(count = hourItems) { index ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(itemHeight.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = (index % state.hourLimit).toString(),
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
                                        items(count = minuteItems) { index ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(itemHeight.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = (index % state.minuteLimit).toString(),
                                                    style = MaterialTheme.typography.labelLarge.copy(
                                                        fontSize = 20.sp
                                                    ),
                                                    color = colorResource(R.color.text_primary)
                                                )
                                            }
                                        }
                                    }
                                    if (state.durationPickerFormat == DurationPickerFormat.H_M_S) {
                                        LazyColumn(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight()
                                                .fadingEdge(fadingEdgeGradient),
                                            state = listStateSeconds,
                                            flingBehavior = flingBehaviorSeconds,
                                            contentPadding = PaddingValues(end = LocalDimen.current.large)
                                        ) {
                                            items(count = secondItems) { index ->
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(itemHeight.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = (index % state.secondLimit).toString(),
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
                                    text = stringResource(negativeButtonText),
                                ) {
                                    onNegative.invoke()
                                }
                                DefaultSpacer()
                                DefaultButton(
                                    modifier = Modifier.weight(1f),
                                    buttonSize = ButtonSize.Medium,
                                    buttonType = ButtonType.Solid,
                                    buttonVariant = ButtonVariant.Primary,
                                    text = stringResource(positiveButtonText),
                                ) {
                                    onPositive.invoke(result.value)
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
            @StringRes title: Int = R.string.edit_timer,
            @StringRes hourTitle: Int = R.string.hours,
            @StringRes minuteTitle: Int = R.string.min,
            @StringRes secondTitle: Int = R.string.sec,
            @StringRes negativeButtonText: Int = R.string.cancel,
            @StringRes positiveButtonText: Int = R.string.set,
            isStatusBarTransparent: Boolean = false,
            state: DurationPickerBottomSheetState = DurationPickerBottomSheetState(),
            onDismiss: () -> Unit = {},
            onNegative: () -> Unit = {},
            onPositive: (result: DurationPickerBottomSheetResult) -> Unit,
        ) {
            DurationPickerBottomSheetDialog().apply {
                this.title = title
                this.hourTitle = hourTitle
                this.minuteTitle = minuteTitle
                this.secondTitle = secondTitle
                this.negativeButtonText = negativeButtonText
                this.positiveButtonText = positiveButtonText
                this.isStatusBarTransparent = isStatusBarTransparent
                this.durationPickerBottomSheetState = state
                this.onDismiss = onDismiss
                this.onNegative = onNegative
                this.onPositive = onPositive
            }.show(fragmentManager, DurationPickerBottomSheetDialog::class.java.simpleName)
        }
    }

}
