package com.noblesoftware.portalcore.component.xml.dynamic_dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.databinding.DialogDynamicBinding
import com.noblesoftware.portalcore.theme.PortalCoreTheme
import com.noblesoftware.portalcore.util.extension.isFalse
import com.noblesoftware.portalcore.util.extension.isTrue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class DefaultDynamicDialog : DialogFragment() {

    private lateinit var binding: DialogDynamicBinding

    private var content: @Composable () -> Unit = {}
    private var onDismiss: () -> Unit = {}
    private var dismissOnBackPress: Boolean = true
    private var dismissOnClickOutside: Boolean = true

    /** if [isStatusBarTransparent] = true -> set this activity and PortalCoreTheme statusBar to transparent */
    private var isStatusBarTransparent: Boolean = false

    override fun getTheme(): Int = R.style.OptionDialogTheme

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setStyle(STYLE_NO_FRAME, R.style.OptionDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_dynamic, container, false
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
                        modifier = Modifier.fillMaxWidth(),
                        color = androidx.compose.ui.graphics.Color.Transparent
                    ) {
                        content.invoke()
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
            window.statusBarColor = androidx.compose.ui.graphics.Color.Transparent.toArgb()
        }

        if (dismissOnBackPress.isFalse()) {
            dialog?.setCancelable(false)
        }

        if (dismissOnClickOutside.isFalse()) {
            dialog?.setCanceledOnTouchOutside(false)
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
            onDismiss: () -> Unit,
            isStatusBarTransparent: Boolean = false,
            dismissOnBackPress: Boolean = true,
            dismissOnClickOutside: Boolean = true,
            tag: String = DefaultDynamicDialog::class.java.simpleName,
            content: @Composable () -> Unit
        ) {
            DefaultDynamicDialog().apply {
                this.isStatusBarTransparent = isStatusBarTransparent
                this.dismissOnBackPress = dismissOnBackPress
                this.dismissOnClickOutside = dismissOnClickOutside
                this.onDismiss = onDismiss
                this.content = content
            }.show(fragmentManager, tag)
        }

        fun AppCompatActivity.dismissDialog(tag: String = DefaultDynamicDialog::class.java.simpleName) {
            (this.supportFragmentManager.findFragmentByTag(tag) as? DialogFragment)?.dismiss()
        }
    }
}