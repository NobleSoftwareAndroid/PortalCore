package com.noblesoftware.portalcore.container

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.util.extension.fileExtension
import com.noblesoftware.portalcore.util.extension.gone
import com.noblesoftware.portalcore.util.extension.isTrue
import com.noblesoftware.portalcore.util.extension.orFalse
import com.noblesoftware.portalcore.util.extension.visible
import com.noblesoftware.portalcore.databinding.ActivityWebviewContainerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


@AndroidEntryPoint
class WebViewContainerFragment : Fragment() {
    private var job: Job? = null

    private val viewModel: WebViewContainerViewModel by viewModels()

    lateinit var binding: ActivityWebviewContainerBinding

    companion object {
        const val TITLE = "title"
        const val URL = "url"
        const val IS_OPEN_FILE_URL = "open_file_url"
        const val IS_OPEN_PREVIEW_RESUME = "preview_resume"
        const val TOKEN = "token"
        fun newInstance(title: String?, url: String?, token: String? = null, isOpenFileUrl: Boolean = false): WebViewContainerFragment {
            return WebViewContainerFragment().apply {
                arguments = bundleOf(
                    TOKEN to token,
                    TITLE to title,
                    URL to url,
                    IS_OPEN_FILE_URL to isOpenFileUrl
                )
            }
        }

        fun openFileUrl(
            context: Context,
            title: String,
            url: String,
            token: String? = null,
        ) {
            FragmentContainerActivity().FragmentTransitionBuilder(context)
                .setFragment(
                    WebViewContainerFragment().apply {
                        arguments = bundleOf(
                            TOKEN to token,
                            TITLE to title,
                            URL to url,
                            IS_OPEN_FILE_URL to true
                        )
                    }
                ).show()
        }

        fun openWebView(
            context: Context,
            title: String,
            url: String,
            token: String? = null,
        ) {
            FragmentContainerActivity().FragmentTransitionBuilder(context)
                .setFragment(
                    WebViewContainerFragment().apply {
                        arguments = bundleOf(
                            TOKEN to token,
                            TITLE to title,
                            URL to url,
                            IS_OPEN_FILE_URL to false
                        )
                    }
                ).show()
        }
    }

    private fun getArgument() {
        viewModel.title = arguments?.getString(TITLE).orEmpty()
        viewModel.url = arguments?.getString(URL).orEmpty()
        viewModel.isOpenFile = arguments?.getBoolean(IS_OPEN_FILE_URL, false).orFalse()
        viewModel.isOpenPreviewResume =
            arguments?.getBoolean(IS_OPEN_PREVIEW_RESUME, false).orFalse()
        viewModel.token = arguments?.getString(TOKEN).orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_webview_container, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgument()
        onInit()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun onInit() {
        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.toolbar.title = viewModel.title
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.progressBar.visible()
                super.onPageStarted(view, url, favicon)
            }

            @SuppressLint("JavascriptInterface")
            override fun onPageFinished(view: WebView?, url: String?) {
                job?.cancel()
                job = null
                job = CoroutineScope(Dispatchers.Main).launch {
                    delay(200)
                    binding.progressBar.gone()
                }
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (viewModel.isOpenFile) {
                    view?.loadUrl(viewModel.url.orEmpty())
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (viewModel.isOpenPreviewResume) {
                    getNewResponse(viewModel.url.orEmpty())
                }
                return null
            }

            private fun getNewResponse(urlRequest: String): WebResourceResponse? {
                return try {
                    val httpClient = OkHttpClient()
                    val request: Request = Request.Builder()
                        .url(urlRequest)
                        .addHeader("Authorization", "Bearer ${arguments?.getString(TOKEN).orEmpty()}")
                        .build()
                    val response: Response = httpClient.newCall(request).execute()
                    WebResourceResponse(
                        null,
                        response.header("content-encoding", "utf-8"),
                        response.body?.byteStream()
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }
        if (viewModel.isOpenFile.isTrue()) {
            val fileExtension = viewModel.url?.fileExtension
            when (fileExtension?.uppercase()) {
                FileType.PDF.toString(), FileType.DOC.toString(), FileType.DOCX.toString(), FileType.TXT.toString(), FileType.RTF.toString() -> {
                    val baseUrl = "https://docs.google.com/gview?embedded=true&url=${viewModel.url}"
                    viewModel.url = baseUrl
                    binding.webView.loadUrl(baseUrl)
                }

                else -> {
                    binding.webView.loadUrl("${viewModel.url}")
                }
            }
        } else if (viewModel.isOpenPreviewResume.isTrue()) {
            val authHeader = "Bearer ${arguments?.getString(TOKEN).orEmpty()}"
            val headers: MutableMap<String, String> = HashMap()
            headers["Authorization"] = authHeader
            binding.webView.loadUrl(viewModel.url.orEmpty(), headers)
        } else {
            binding.webView.loadUrl(viewModel.url ?: "")
        }
    }

    enum class FileType {
        JPEG,
        JPG,
        PNG,
        DOC,
        DOCX,
        TXT,
        RTF,
        PDF,
    }

}