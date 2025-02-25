package com.noblesoftware.portalcore.component.xml

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import com.noblesoftware.portalcore.model.WebViewFontStyle
import com.noblesoftware.portalcore.util.extension.orZero
import com.noblesoftware.portalcore.util.extension.toMathEqFormatted


/**
 * Created by Hafizh Anbiya on 16 Feb 2023
 * https://github.com/Fizhu
 */

@SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
class CustomWebview(context: Context, attrs: AttributeSet?) : WebView(context, attrs) {

    var preDefinedConfig: String? = null
    var zoomEnable: Boolean = false
    var touchEnable: Boolean = true
    var maxLines: Int? = null
    var useDefaultTableStyle: Boolean = false
    var webViewFontStyle: WebViewFontStyle? = null
    private val defaultTableStyle = "table,th,td {" +
            "    border: 1px solid #bbb;" +
            "    padding-top: 4px;" +
            "    padding-right: 8px;" +
            "    padding-bottom: 4px;" +
            "    padding-left: 8px;" +
            "    border-collapse: collapse;" +
            "  }"

    private fun supportMaxLineStyle(maxLines: Int) =
        "style=\"--line-clamp: ${maxLines}; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: var(--line-clamp); -webkit-box-orient: vertical;\""

    private fun WebViewFontStyle.toCSS(): String {
        val value = TypedValue()
        resources.getValue(fontFamily, value, true)
        val font = value.string.toString().split("/").lastOrNull()
        val color = String.format(
            "#%06x",
            ContextCompat.getColor(context, this.color) and 0xffffff
        )

        return "@font-face {font-family: interregular;" +
                "src: url(file:///android_res/font/${font});}\n" +
                "body {font-size: ${size}px; font-weight:${if (bold) "${FontWeight.Bold.weight}" else "${FontWeight.Normal.weight}"}; color: ${color}; text-align: left; font-family: interregular;}\n"
    }

    var config: String? = null
        set(value) {
            field = value?.replace("\n", "")?.replace(" ", "")
        }
    var text: String? = null
        set(value) {
            field = value
            loadDataWithBaseURL(
                "about:blank",
                "<html><head>" +
                        "<style>" +
                        "${if (useDefaultTableStyle) defaultTableStyle else ""}" +
                        "${if (webViewFontStyle != null) webViewFontStyle?.toCSS() else ""}" +
                        "*{ webkit-user-select: none; user-select: none; }" +
                        "p{ margin: 0 !important; padding: 0 !important; }" +
                        "body{ margin: 0 !important; padding: 0 !important; }" +
                        "</style>" +
                        "<script type=\"text/x-mathjax-config\">" +
                        config +
                        "</script>" +
                        "<script type=\"text/javascript\" async src=\"file:///android_asset/MathJax/MathJax.js?config=" + preDefinedConfig + "\"></script>" +
                        "</head>" +
                        "<body>" +
                        "<div ${if (maxLines != null) supportMaxLineStyle(maxLines.orZero()) else ""} >" +
                        text?.toMathEqFormatted() +
                        "</div>" +
                        "</body>" +
                        "</html>", "text/html", "utf-8", ""
            )
        }

    init {
        // if text is set in XML, call setText() with that text
        // val a = getContext().obtainStyledAttributes(
        //     attrs, R.styleable.CustomWebview
        // )
        // val text = a.getString(R.styleable.CustomWebview_android_text)
        // val zoom = a.getBoolean(R.styleable.CustomWebview_zoomEnable, false)
        // val table = a.getBoolean(R.styleable.CustomWebview_useDefaultTableStyle, false)

        // default config for MathJax
        config = "MathJax.Hub.Config({" +
                "    extensions: ['fast-preview.js']," +
                "    messageStyle: 'none'," +
                "    \"fast-preview\": {" +
                "      disabled: false" +
                "    }," +
                "    CommonHTML: {" +
                "      linebreaks: { automatic: true, width: \"container\" }" +
                "    }," +
                "    tex2jax: {" +
                "      inlineMath: [ ['$','$'] ]," +
                "      displayMath: [ ['$$','$$'] ]," +
                "      processEscapes: true" +
                "    }," +
                "    TeX: {" +
                "      extensions: [\"file:///android_asset/MathJax/extensions/TeX/mhchem.js\"]," +
                "      mhchem: {legacy: false}" +
                "    }" +
                "});"
        preDefinedConfig = "TeX-MML-AM_CHTML"

        // if (!TextUtils.isEmpty(text)) this.text = text
        // this.zoomEnable = zoom
        // this.useDefaultTableStyle = table
        // a.recycle()

        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT

        // if (zoomEnable) {
        //     settings.builtInZoomControls = true
        //     settings.displayZoomControls = false
        // }

        setBackgroundColor(Color.TRANSPARENT)

        // render MathJax once page finishes loading
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                view.loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);")
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (touchEnable) {
            super.onTouchEvent(event)
        } else {
            // Disable touch events
            false
        }
    }

    override fun performClick(): Boolean {
        return if (touchEnable) {
            super.performClick()
        } else {
            // Disable click events
            false
        }
    }
}