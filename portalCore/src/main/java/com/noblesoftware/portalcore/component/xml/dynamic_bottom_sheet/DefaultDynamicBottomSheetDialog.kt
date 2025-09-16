package com.noblesoftware.portalcore.component.xml.dynamic_bottom_sheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.databinding.BottomSheetDialogDynamicBinding
import com.noblesoftware.portalcore.enums.BottomSheetActionType
import com.noblesoftware.portalcore.theme.PortalCoreTheme
import com.noblesoftware.portalcore.util.extension.isFalse
import com.noblesoftware.portalcore.util.extension.isTrue
import com.noblesoftware.portalcore.util.extension.visibleWhen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class DefaultDynamicBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDialogDynamicBinding

    private var content: @Composable () -> Unit = {}

    /** if [isStatusBarTransparent] = true -> set this activity and PortalCoreTheme statusBar to transparent */
    private var isStatusBarTransparent: Boolean = false

    private var initialBottomSheetState = BottomSheetBehavior.STATE_EXPANDED
    private var dismissible: Boolean = false

    private var buttonFirstEnable: Boolean = true
    private var buttonFirstText: Int = R.string.close
    private var buttonFirstType: BottomSheetActionType = BottomSheetActionType.NEUTRAL
    private var buttonFirstOnClick: () -> Unit = {}

    private var buttonSecondEnable: Boolean = false
    private var buttonSecondText: Int = R.string.okay
    private var buttonSecondType: BottomSheetActionType = BottomSheetActionType.PRIMARY_SOLID
    private var buttonSecondOnClick: () -> Unit = {}

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
                R.layout.bottom_sheet_dialog_dynamic,
                container,
                false
            )
        binding.lifecycleOwner = viewLifecycleOwner
        val view = binding.root
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PortalCoreTheme(
                    window = requireActivity().window,
                    isStatusBarTransparent = isStatusBarTransparent
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .nestedScroll(rememberNestedScrollInteropConnection())
                            .padding(bottom = if (buttonFirstEnable || buttonSecondEnable) 120.dp else 0.dp),
                        color = colorResource(id = R.color.background_body)
                    ) {
                        content.invoke()
                    }
                }
            }
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        if (buttonFirstEnable.isFalse() && buttonSecondEnable.isFalse()) {
            return super.onCreateDialog(savedInstanceState)
        }

        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            (it as BottomSheetDialog).findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)

            val containerLayout =
                it.findViewById<FrameLayout>(com.google.android.material.R.id.container)

            val buttons = bottomSheetDialog.layoutInflater.inflate(
                R.layout.bottom_sheet_dialog_dynamic_action,
                null
            )
            val buttonFirst = buttons.findViewById<MaterialButton>(R.id.btn_first)
            val buttonSecond = buttons.findViewById<MaterialButton>(R.id.btn_second)

            buttonFirst.visibleWhen(buttonFirstEnable)
            buttonSecond.visibleWhen(buttonSecondEnable)
            buttonFirst.setButtonType(requireContext(), buttonFirstType)
            buttonSecond.setButtonType(requireContext(), buttonSecondType)
            buttonFirst.text = ContextCompat.getString(requireContext(), buttonFirstText)
            buttonSecond.text = ContextCompat.getString(requireContext(), buttonSecondText)

            buttonFirst.setOnClickListener {
                buttonFirstOnClick.invoke()
                if (dismissible) {
                    dismiss()
                }
            }
            buttonSecond.setOnClickListener {
                buttonSecondOnClick.invoke()
                if (dismissible) {
                    dismiss()
                }
            }

            buttons.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                @SuppressLint("InternalInsetResource", "DiscouragedApi")
                val resourceId =
                    resources.getIdentifier("navigation_bar_height", "dimen", "android")
                buttons.setPadding(
                    0, 0, 0,
                    resources.getDimensionPixelSize(resourceId)
                )
            }

            /** if the initial fullscreen */
            if (initialBottomSheetState == BottomSheetBehavior.STATE_EXPANDED) {
                it.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                    ?.let {
                        val bottomSheetBehavior = BottomSheetBehavior.from(it)
                        bottomSheetBehavior.skipCollapsed = true
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
            }
            bottomSheetDialog.setCancelable(dismissible)

            containerLayout?.addView(buttons)
        }

        return bottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isStatusBarTransparent.isTrue()) {
            val window = requireActivity().window
            window.statusBarColor = androidx.compose.ui.graphics.Color.Transparent.toArgb()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss.invoke()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            isStatusBarTransparent: Boolean = false,
            isDismissible: Boolean = true,
            isInitialFullscreen: Boolean = false,
            buttonFirstEnable: Boolean = true,
            buttonSecondEnable: Boolean = false,
            @StringRes buttonFirstText: Int = R.string.close,
            @StringRes buttonSecondText: Int = R.string.okay,
            buttonFirstType: BottomSheetActionType = BottomSheetActionType.NEUTRAL,
            buttonSecondType: BottomSheetActionType = BottomSheetActionType.PRIMARY_SOLID,
            tag: String = DefaultDynamicBottomSheetDialog::class.java.simpleName,
            buttonFirstOnClick: () -> Unit = {},
            buttonSecondOnClick: () -> Unit = {},
            onDismiss: () -> Unit = {},
            content: @Composable () -> Unit
        ) {
            DefaultDynamicBottomSheetDialog().apply {
                this.isStatusBarTransparent = isStatusBarTransparent
                this.dismissible = isDismissible
                this.initialBottomSheetState =
                    if (isInitialFullscreen) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_HALF_EXPANDED
                this.buttonFirstEnable = buttonFirstEnable
                this.buttonSecondEnable = buttonSecondEnable
                this.buttonFirstText = buttonFirstText
                this.buttonSecondText = buttonSecondText
                this.buttonFirstType = buttonFirstType
                this.buttonSecondType = buttonSecondType
                this.buttonFirstOnClick = buttonFirstOnClick
                this.buttonSecondOnClick = buttonSecondOnClick
                this.onDismiss = onDismiss
                this.content = content
            }.show(fragmentManager, tag)
        }

        fun AppCompatActivity.dismiss(tag: String = DefaultDynamicBottomSheetDialog::class.java.simpleName) {
            (this.supportFragmentManager.findFragmentByTag(tag) as? DialogFragment)?.dismiss()
        }
    }
}

private fun MaterialButton.setButtonType(context: Context, buttonType: BottomSheetActionType) {
    when (buttonType) {
        BottomSheetActionType.PRIMARY_SOLID -> {
            this.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_solid_bg))
            this.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_solid_bg))
            this.setTextColor(ContextCompat.getColor(context, R.color.primary_solid_color))
        }

        BottomSheetActionType.PRIMARY_OUTLINED -> {
            this.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background_body))
            this.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_solid_bg))
            this.setTextColor(ContextCompat.getColor(context, R.color.primary_solid_bg))
        }

        BottomSheetActionType.DANGER_SOLID -> {
            this.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.danger_solid_bg))
            this.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.danger_solid_bg))
            this.setTextColor(ContextCompat.getColor(context, R.color.danger_solid_color))
        }

        BottomSheetActionType.DANGER_OUTLINED -> {
            this.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background_body))
            this.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.danger_plain_color))
            this.setTextColor(ContextCompat.getColor(context, R.color.danger_plain_color))
        }

        BottomSheetActionType.NEUTRAL -> {
            this.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.background_body))
            this.strokeColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    R.color.neutral_outlined_border
                )
            )
            this.setTextColor(ContextCompat.getColor(context, R.color.neutral_outlined_color))
        }
    }
}