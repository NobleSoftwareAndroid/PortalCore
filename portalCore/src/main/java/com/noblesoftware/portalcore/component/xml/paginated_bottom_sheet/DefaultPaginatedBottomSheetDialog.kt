package com.noblesoftware.portalcore.component.xml.paginated_bottom_sheet

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.compose.DefaultEmptyState
import com.noblesoftware.portalcore.component.compose.DefaultProgress
import com.noblesoftware.portalcore.component.compose.DefaultSpacer
import com.noblesoftware.portalcore.component.compose.DefaultTextInput
import com.noblesoftware.portalcore.component.compose.DefaultTextInputIcon
import com.noblesoftware.portalcore.component.xml.options_bottom_sheet.BottomSheetEvent
import com.noblesoftware.portalcore.component.xml.options_bottom_sheet.BottomSheetViewModel
import com.noblesoftware.portalcore.databinding.BottomSheetDialogBinding
import com.noblesoftware.portalcore.enums.BottomSheetType
import com.noblesoftware.portalcore.model.SelectOption
import com.noblesoftware.portalcore.theme.LocalDimen
import com.noblesoftware.portalcore.theme.LocalShapes
import com.noblesoftware.portalcore.theme.PortalCoreTheme
import com.noblesoftware.portalcore.util.extension.isFalse
import com.noblesoftware.portalcore.util.extension.isTrue
import com.noblesoftware.portalcore.util.extension.orZero
import com.noblesoftware.portalcore.util.extension.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
open class DefaultPaginatedBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDialogBinding
    private val viewModel: BottomSheetViewModel by viewModels()

    private var bottomSheetType = BottomSheetType.SINGLE_SELECTION_WITH_SEARCH

    @StringRes
    private var title: Int = R.string.empty_string
    private var isResetEnable: Boolean = false

    /** if [isStatusBarTransparent] = true -> set this activity and PortalCoreTheme statusBar to transparent */
    private var isStatusBarTransparent: Boolean = false
    private var searchHint = ""
    private var searchValue = ""
    private var onSelected: (List<SelectOption>) -> Unit = {}
    private var onDismiss: () -> Unit = {}
    private var onSearch: (String) -> Unit = {}
    private var lazyPagingItems: LazyPagingItems<SelectOption>? = null
    private var initialBottomSheetState = BottomSheetBehavior.STATE_EXPANDED
    private var selectedItem: SelectOption? = null
    private lateinit var paginatedBottomSheetData: PaginatedBottomSheetData

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
                val focusRequester = remember { FocusRequester() }
                val isLoading = remember { mutableStateOf(false) }
                val isProgressLoading = remember { mutableStateOf(false) }
                val resetButtonAlpha = animateFloatAsState(
                    targetValue = if (state.selectOptions.any { option -> option.isSelected } ||
                        state.filteredSelectOptions.any { option -> option.isSelected }) 1f else 0f,
                    label = ""
                )

                LaunchedEffect(searchValue) {
                    onEvent(BottomSheetEvent.OnSearch(searchValue))
                    if (searchValue.isNotBlank()) {
                        onSearch.invoke(searchValue)
                    }
                }

                // handle autofocus search field on launch screen
                LaunchedEffect(true) {
                    delay(250)
                    focusRequester.requestFocus()
                }

                LaunchedEffect(lazyPagingItems?.loadState?.refresh) {
                    when (lazyPagingItems?.loadState?.refresh) {
                        is LoadState.Loading -> {
                            isLoading.value = true
                        }

                        !is LoadState.Loading -> {
                            isLoading.value = false
                            if (lazyPagingItems?.loadState?.refresh is LoadState.Error) {
                                lazyPagingItems?.let {
                                    showErrorSnackBar(
                                        view,
                                        lazyPagingItems?.loadState?.refresh as LoadState.Error
                                    )
                                }
                            }
                        }

                        else -> {}
                    }
                }

                LaunchedEffect(lazyPagingItems?.loadState?.append) {
                    when (lazyPagingItems?.loadState?.append) {
                        is LoadState.Loading -> {
                            isProgressLoading.value = true
                        }

                        !is LoadState.Loading -> {
                            isProgressLoading.value = false
                            if (lazyPagingItems?.loadState?.append is LoadState.Error) {
                                lazyPagingItems?.let {
                                    showErrorSnackBar(
                                        view,
                                        lazyPagingItems?.loadState?.append as LoadState.Error
                                    )
                                }
                            }
                        }

                        else -> {}
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
                            modifier = Modifier.then(Modifier.fillMaxSize())
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
                            if (bottomSheetType == BottomSheetType.SINGLE_SELECTION_WITH_SEARCH) {
                                DefaultTextInput(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = LocalDimen.current.regular)
                                        .focusRequester(focusRequester),
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
                                                    onSearch.invoke("")
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
                                    imeAction = ImeAction.Search,
                                    onValueChange = {
                                        onEvent(BottomSheetEvent.OnSearch(it))
                                        onSearch.invoke(it)
                                    },
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            onSearch.invoke(state.keywords)
                                        }
                                    )
                                )
                                DefaultSpacer(height = LocalDimen.current.regular)
                            }
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(
                                    start = LocalDimen.current.regular,
                                    end = LocalDimen.current.regular,
                                    bottom = LocalDimen.current.extraLarge
                                ),
                                verticalArrangement = Arrangement.spacedBy(LocalDimen.current.small)
                            ) {
                                // Loading
                                if (isLoading.value) {
                                    item {
                                        Column(
                                            modifier = Modifier.padding(
                                                top = LocalDimen.current.extraLarge
                                            ),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            DefaultProgress(modifier = Modifier.fillMaxSize())
                                        }
                                    }
                                }
                                if (isEmptyState(lazyPagingItems)) {
                                    item {
                                        DefaultSpacer(height = LocalDimen.current.extraRegular)
                                        DefaultEmptyState(
                                            modifier = Modifier.fillMaxWidth(),
                                            title = if (state.keywords.isBlank() || state.keywords.length < paginatedBottomSheetData.searchQueryMinimalChar) paginatedBottomSheetData.initialTitle else paginatedBottomSheetData.title,
                                            message = if (state.keywords.isBlank()) {
                                                paginatedBottomSheetData.initialSubtitle
                                            } else if (state.keywords.length < paginatedBottomSheetData.searchQueryMinimalChar) {
                                                paginatedBottomSheetData.subtitle
                                            } else stringResource(id = R.string.empty_string)
                                        )
                                    }
                                } else {
                                    lazyPagingItems?.let { lazyPagingItems ->
                                        items(
                                            count = lazyPagingItems.itemCount,
                                            key = lazyPagingItems.itemKey { key -> key.id.orZero() },
                                        ) { index ->
                                            var item = lazyPagingItems[index]
                                            if (item?.id == selectedItem?.id) {
                                                item = item?.copy(isSelected = true)
                                            }
                                            item?.let {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(LocalDimen.current.small))
                                                        .fillMaxWidth()
                                                        .then(
                                                            if (item.isSelected) {
                                                                Modifier.background(
                                                                    color = colorResource(
                                                                        id = R.color.primary_plain_active_bg
                                                                    )
                                                                )
                                                            } else {
                                                                Modifier
                                                            }
                                                        )
                                                        .then(
                                                            if (item.enabled) {
                                                                Modifier.clickable(
                                                                    interactionSource = remember { MutableInteractionSource() },
                                                                    indication = ripple(
                                                                        color = colorResource(id = R.color.primary_plain_color),
                                                                    ),
                                                                    onClick = {
                                                                        onSelected.invoke(
                                                                            listOf(
                                                                                item
                                                                            )
                                                                        )
                                                                        dismiss()
                                                                    },
                                                                )
                                                            } else Modifier
                                                        )
                                                        .padding(
                                                            horizontal = LocalDimen.current.default,
                                                            vertical = LocalDimen.current.regular
                                                        )
                                                ) {
                                                    Text(
                                                        text = if (item.nameId != null) stringResource(
                                                            id = item.nameId
                                                                ?: R.string.empty_string
                                                        ) else item.name,
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            color = colorResource(
                                                                id = if (item.isSelected) R.color.primary_plain_color
                                                                else if (item.enabled.isFalse()) R.color.primary_plain_disabled_color
                                                                else R.color.text_primary
                                                            )
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                // Progress loading page
                                if (isProgressLoading.value) {
                                    item {
                                        Column(
                                            modifier = Modifier.padding(
                                                top = LocalDimen.current.large,
                                                bottom = LocalDimen.current.extraLarge
                                            ),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            DefaultProgress(modifier = Modifier.fillMaxSize())
                                        }
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

    private fun ComposeView.showErrorSnackBar(
        view: View,
        errorState: LoadState.Error? = null
    ) {
        val error = errorState?.error
        if (error != null) {
            view.showSnackBar(
                message = (if (error.message.equals(R.string.please_check_internet_connection.toString())) context.getString(
                    R.string.please_check_internet_connection
                ) else error.message).orEmpty(),
                isSuccess = false,
                textDismissButton = "",
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isStatusBarTransparent.isTrue()) {
            val window = requireActivity().window
            window.statusBarColor = Color.Transparent.toArgb()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet: FrameLayout =
                (it as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)
                    ?: return@setOnShowListener
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.skipCollapsed =
                initialBottomSheetState == BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = initialBottomSheetState
        }
        return bottomSheetDialog
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
            bottomSheetType: BottomSheetType = BottomSheetType.SINGLE_SELECTION_WITH_SEARCH,
            @StringRes title: Int = R.string.empty_string,
            searchHint: String = "",
            searchValue: String = "",
            isResetEnable: Boolean = false,
            isStatusBarTransparent: Boolean = false,
            initialBottomSheetState: Int = BottomSheetBehavior.STATE_EXPANDED,
            paginatedBottomSheetData: PaginatedBottomSheetData,
            selectedItem: SelectOption? = null,
            lazyPagingItems: LazyPagingItems<SelectOption>,
            onDismiss: () -> Unit = {},
            onSearch: (String) -> Unit = {},
            onSelected: (List<SelectOption>) -> Unit = {}
        ) {
            DefaultPaginatedBottomSheetDialog().apply {
                this.bottomSheetType = bottomSheetType
                this.title = title
                this.isResetEnable = isResetEnable
                this.isStatusBarTransparent = isStatusBarTransparent
                this.initialBottomSheetState = initialBottomSheetState
                this.searchHint = searchHint
                this.searchValue = searchValue
                this.selectedItem = selectedItem
                this.paginatedBottomSheetData = paginatedBottomSheetData
                this.lazyPagingItems = lazyPagingItems
                this.onDismiss = onDismiss
                this.onSearch = onSearch
                this.onSelected = onSelected
            }.show(fragmentManager, DefaultPaginatedBottomSheetDialog::class.java.simpleName)
        }
    }

    private fun isEmptyState(
        lazyPagingItems: LazyPagingItems<SelectOption>?
    ) =
        ((lazyPagingItems?.loadState?.source?.refresh is LoadState.NotLoading || lazyPagingItems?.loadState?.source?.refresh is LoadState.Error)) && lazyPagingItems.itemCount == 0
}

data class PaginatedBottomSheetData(
    val title: String,
    val subtitle: String,
    val initialTitle: String,
    val initialSubtitle: String,
    val searchQueryMinimalChar: Int
)
