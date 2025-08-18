package com.noblesoftware.portalcore.component.xml.dynamic_fullscreen_dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.component.xml.dynamic_bottom_sheet.DefaultDynamicBottomSheetDialog
import com.noblesoftware.portalcore.databinding.DialogDynamicFragmentBinding
import com.noblesoftware.portalcore.theme.PortalCoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class DefaultDynamicFragment : DialogFragment() {

    private lateinit var binding: DialogDynamicFragmentBinding

    private var content: @Composable () -> Unit = {}

    private var onDismiss: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_dynamic_fragment,
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
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .nestedScroll(rememberNestedScrollInteropConnection()),
                        color = colorResource(id = R.color.background_body)
                    ) {
                        content.invoke()
                    }
                }
            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss.invoke()
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            tag: String = DefaultDynamicFragment::class.java.simpleName,
            onDismiss: () -> Unit = {},
            content: @Composable () -> Unit
        ) {
            DefaultDynamicFragment().apply {
                this.onDismiss = onDismiss
                this.content = content
            }.show(fragmentManager, tag)
        }

        fun AppCompatActivity.dismiss(tag: String = DefaultDynamicBottomSheetDialog::class.java.simpleName) {
            (this.supportFragmentManager.findFragmentByTag(tag) as? DialogFragment)?.dismiss()
        }
    }
}