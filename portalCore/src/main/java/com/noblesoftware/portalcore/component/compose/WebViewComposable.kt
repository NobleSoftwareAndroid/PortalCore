package com.noblesoftware.portalcore.component.compose

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import com.noblesoftware.portalcore.component.xml.CustomWebview

@Composable
fun WebViewComposable(
    modifier: Modifier = Modifier,
    content: String,
    zoomEnable: Boolean = false,
    onWebViewCreated: (CustomWebview) -> Unit = {},
    update: (CustomWebview) -> Unit = NoOpUpdate
) {
    val context = LocalContext.current
    val webView = remember {
        CustomWebview(context = context, attrs = null).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.loadWithOverviewMode = true
            settings.domStorageEnabled = true
            isVerticalScrollBarEnabled = false
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
            if (zoomEnable) {
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
            }
        }
    }

    onWebViewCreated(webView)

    AndroidView(
        modifier = modifier,
        factory = { webView },
        update = { view ->
            if (view.text != content) {
                view.text = content
            }
            update.invoke(view)
        }
    )
}