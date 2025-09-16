package com.noblesoftware.portalcore.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.PortalCoreTheme

/**
 * Created by Hafizh Anbiya on 06 September 2024
 * https://github.com/Fizhu
 */

class DynamicFragment : Fragment() {

    private var content: @Composable () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dynamic, container, false)
        view.findViewById<ComposeView>(R.id.compose_view).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PortalCoreTheme {
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

    companion object {
        fun newInstance(content: @Composable () -> Unit) =
            DynamicFragment().apply { this.content = content }
    }

}