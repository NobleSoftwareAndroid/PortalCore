package com.noblesoftware.portalcore.component.xml.options_bottom_sheet

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.DefaultEmptyState
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.DefaultTextInput
import com.noblesoftware.portalcore.component.compose.DefaultTextInputIcon
import com.noblesoftware.portalcore.databinding.BottomSheetDialogBinding
import com.noblesoftware.portalcore.enums.BottomSheetType
import com.noblesoftware.portalcore.model.SelectOption
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes
import com.noblesoftware.portalcore.theme.PortalCoreTheme
import com.noblesoftware.portalcore.util.extension.isFalse
import com.noblesoftware.portalcore.util.extension.isTrue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class DefaultBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDialogBinding
    private val viewModel: BottomSheetViewModel by viewModels()

    private var bottomSheetType = BottomSheetType.SINGLE_SELECTION

    @StringRes
    private var title: Int = R.string.empty_string
    private var isResetEnable: Boolean = false

    /** if [isStatusBarTransparent] = true -> set this activity and PortalCoreTheme statusBar to transparent */
    private var isStatusBarTransparent: Boolean = false
    private var searchHint = ""
    private var emptyState: @Composable () -> Unit = {}
    private var options: List<SelectOption> = listOf()
    private var onSelected: (List<SelectOption>) -> Unit = {}

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
                val onEvent = viewModel::onEvent
                val state = viewModel.state.collectAsState().value
                val resetButtonAlpha = animateFloatAsState(
                    targetValue = if (state.selectOptions.any { option -> option.isSelected } ||
                        state.filteredSelectOptions.any { option -> option.isSelected }) 1f else 0f,
                    label = ""
                )

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
                            modifier = Modifier.then(
                                if (bottomSheetType == BottomSheetType.SINGLE_SELECTION_WITH_SEARCH ||
                                    bottomSheetType == BottomSheetType.MULTIPLE_SELECTION_WITH_SEARCH
                                ) {
                                    Modifier.fillMaxSize()
                                } else {
                                    Modifier.fillMaxWidth()
                                }
                            )
                        ) {
                            DefaultSpacer(height = LocalDimen.current.medium)
                            if (title != R.string.empty_string || isResetEnable) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = LocalDimen.current.regular),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = stringResource(id = title),
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            colorResource(id = R.color.text_primary),
                                        )
                                    )
                                    if (isResetEnable) {
                                        Box(modifier = Modifier.width(LocalDimen.current.regular))
                                        TextButton(
                                            modifier = Modifier
                                                .height(dimensionResource(id = R.dimen.button_small_height))
                                                .alpha(resetButtonAlpha.value),
                                            shape = LocalShapes.small,
                                            onClick = {
                                                onEvent(BottomSheetEvent.ResetSelected)
                                            },
                                        ) {
                                            Text(
                                                text = stringResource(R.string.reset),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = colorResource(id = R.color.primary_outlined_color)
                                                )
                                            )
                                        }
                                    }
                                }
                                DefaultSpacer(height = if (isResetEnable) LocalDimen.current.medium else LocalDimen.current.regular)
                            }
                            if (bottomSheetType == BottomSheetType.SINGLE_SELECTION_WITH_SEARCH ||
                                bottomSheetType == BottomSheetType.MULTIPLE_SELECTION_WITH_SEARCH
                            ) {
                                DefaultTextInput(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = LocalDimen.current.regular),
                                    value = state.keywords,
                                    leadingIcon = {
                                        DefaultTextInputIcon(
                                            modifier = Modifier,
                                            icon = painterResource(id = R.drawable.ic_search),
                                        )
                                    },
                                    trailingIcon = if (state.keywords.isNotBlank()) {
                                        {
                                            IconButton(
                                                onClick = {
                                                    onEvent(BottomSheetEvent.OnSearch(""))
                                                },
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_close),
                                                    tint = colorResource(id = R.color.text_icon),
                                                    contentDescription = ""
                                                )
                                            }
                                        }
                                    } else {
                                        null
                                    },
                                    placeholder = searchHint,
                                    onValueChange = {
                                        onEvent(BottomSheetEvent.OnSearch(it))
                                    },
                                )
                                DefaultSpacer(height = LocalDimen.current.regular)
                            }
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(
                                    start = LocalDimen.current.regular,
                                    end = LocalDimen.current.regular,
                                    bottom = if ((bottomSheetType == BottomSheetType.MULTIPLE_SELECTION && state.selectOptions.isNotEmpty()) || bottomSheetType == BottomSheetType.MULTIPLE_SELECTION_WITH_SEARCH) 180.dp else LocalDimen.current.extraLarge
                                ),
                                verticalArrangement = Arrangement.spacedBy(
                                    if (bottomSheetType == BottomSheetType.MULTIPLE_SELECTION ||
                                        bottomSheetType == BottomSheetType.MULTIPLE_SELECTION_WITH_SEARCH
                                    ) LocalDimen.current.small else LocalDimen.current.zero
                                )
                            ) {
                                val isEmpty =
                                    if (state.keywords.isEmpty()) state.selectOptions.isEmpty() else state.filteredSelectOptions.isEmpty()

                                if (isEmpty.isFalse()) {
                                    items(if (state.keywords.isEmpty()) state.selectOptions.size else state.filteredSelectOptions.size) {
                                        val selectOption =
                                            if (state.keywords.isEmpty()) state.selectOptions[it] else state.filteredSelectOptions[it]
                                        if (bottomSheetType != BottomSheetType.MULTIPLE_SELECTION && bottomSheetType != BottomSheetType.MULTIPLE_SELECTION_WITH_SEARCH) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(LocalDimen.current.small))
                                                    .fillMaxWidth()
                                                    .then(
                                                        if (selectOption.isSelected) {
                                                            Modifier.background(
                                                                color = colorResource(
                                                                    id = R.color.primary_plain_active_bg
                                                                )
                                                            )
                                                        } else {
                                                            Modifier
                                                        }
                                                    )
                                                    .clickable(
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        indication = ripple(
                                                            color = colorResource(id = R.color.primary_plain_color),
                                                        ),
                                                        onClick = {
                                                            onSelected.invoke(
                                                                arrayListOf(
                                                                    selectOption
                                                                )
                                                            )
                                                            dismiss()
                                                        },
                                                    )
                                                    .padding(
                                                        horizontal = LocalDimen.current.default,
                                                        vertical = LocalDimen.current.regular
                                                    )
                                            ) {
                                                Text(
                                                    text = if (selectOption.nameId != null) stringResource(
                                                        id = selectOption.nameId
                                                            ?: R.string.empty_string
                                                    ) else selectOption.name,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        color = colorResource(id = if (selectOption.isSelected) R.color.primary_plain_color else R.color.text_primary)
                                                    )
                                                )
                                            }
                                        } else {
                                            Row(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(LocalDimen.current.small))
                                                    .fillMaxWidth()
                                                    .then(
                                                        if (selectOption.isSelected) {
                                                            Modifier.background(
                                                                color = colorResource(
                                                                    id = R.color.primary_plain_active_bg
                                                                )
                                                            )
                                                        } else {
                                                            Modifier
                                                        }
                                                    )
                                                    .clickable(
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        indication = ripple(
                                                            color = colorResource(id = R.color.primary_plain_color),
                                                        ),
                                                        onClick = {
                                                            onEvent(
                                                                BottomSheetEvent.OnSelect(
                                                                    selectOption
                                                                )
                                                            )
                                                        },
                                                    )
                                                    .padding(
                                                        horizontal = LocalDimen.current.default,
                                                        vertical = LocalDimen.current.regular
                                                    ),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                CompositionLocalProvider(
                                                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                                                ) {
                                                    Checkbox(
                                                        checked = selectOption.isSelected,
                                                        onCheckedChange = {
                                                            onEvent(
                                                                BottomSheetEvent.OnSelect(
                                                                    selectOption
                                                                )
                                                            )
                                                        })
                                                }
                                                DefaultSpacer(width = 14.dp)
                                                Text(
                                                    text = if (selectOption.nameId != null) stringResource(
                                                        id = selectOption.nameId
                                                            ?: R.string.empty_string
                                                    ) else selectOption.name,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        color = colorResource(id = if (selectOption.isSelected) R.color.primary_plain_color else R.color.text_primary)
                                                    )
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    item {
                                        emptyState()
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onEvent(BottomSheetEvent.InitSelectOptions(options))

        if (isStatusBarTransparent.isTrue()) {
            val window = requireActivity().window
            window.statusBarColor = androidx.compose.ui.graphics.Color.Transparent.toArgb()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (bottomSheetType != BottomSheetType.MULTIPLE_SELECTION && bottomSheetType != BottomSheetType.MULTIPLE_SELECTION_WITH_SEARCH) {
            return super.onCreateDialog(savedInstanceState)
        }

        if (bottomSheetType == BottomSheetType.MULTIPLE_SELECTION && this.options.isEmpty()) {
            return super.onCreateDialog(savedInstanceState)
        }

        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            (it as BottomSheetDialog).findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)

            val containerLayout =
                it.findViewById<FrameLayout>(com.google.android.material.R.id.container)

            val buttons = bottomSheetDialog.layoutInflater.inflate(
                R.layout.button_select_option_dialog,
                null
            )
            val buttonSave = buttons.findViewById<MaterialButton>(R.id.btn_save)
            val buttonCancel = buttons.findViewById<MaterialButton>(R.id.btn_cancel)

            buttonSave.setOnClickListener {
                onSelected.invoke(viewModel.state.value.selectOptions.filter { option ->
                    option.isSelected
                })
                dismiss()
            }
            buttonCancel.setOnClickListener {
                dismiss()
            }

            buttons.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM
            }
            containerLayout?.addView(buttons)
        }
        return bottomSheetDialog
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            bottomSheetType: BottomSheetType,
            @StringRes title: Int = R.string.empty_string,
            searchHint: String = "",
            isResetEnable: Boolean = false,
            isStatusBarTransparent: Boolean = false,
            emptyState: @Composable () -> Unit = {
                if (bottomSheetType == BottomSheetType.SINGLE_SELECTION_WITH_SEARCH || bottomSheetType == BottomSheetType.MULTIPLE_SELECTION_WITH_SEARCH) {
                    DefaultSpacer(height = LocalDimen.current.extraRegular)
                }
                DefaultEmptyState(
                    modifier = Modifier.fillMaxWidth(),
                    icon = painterResource(id = R.drawable.img_announcement_empty),
                    title = stringResource(id = R.string.no_data_available),
                    message = stringResource(id = R.string.data_related_will_be_shown_here)
                )
            },
            options: List<SelectOption> = listOf(),
            onSelected: (List<SelectOption>) -> Unit = {}
        ) {
            DefaultBottomSheetDialog().apply {
                this.bottomSheetType = bottomSheetType
                this.title = title
                this.isResetEnable = isResetEnable
                this.isStatusBarTransparent = isStatusBarTransparent
                this.searchHint = searchHint
                this.emptyState = emptyState
                this.options = options
                this.onSelected = onSelected
            }.show(fragmentManager, DefaultBottomSheetDialog::class.java.simpleName)
        }
    }
}
