package com.canbe.contactbackup.ui.webview.ui

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.canbe.contactbackup.ui.webview.ui.theme.CustomWebViewTheme
import com.canbe.contactbackup.ui.webview.ui.theme.FixedTextStyle
import timber.log.Timber

@SuppressLint("SetJavaScriptEnabled")
@ExperimentalMaterial3Api
@Composable
fun WebViewScreen(
    title: String? = null,
    url: String,
    onCloseClick: () -> Unit,
    onSendEmailClick: (emailList: Array<String>) -> Unit
) {
    var progress by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                navigationIcon = {
                    Spacer(modifier = Modifier.size(48.dp))
                },
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 3.dp),
                            style = FixedTextStyle(16.sp),
                            textAlign = TextAlign.Center,
                            text = title ?: ""
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "설정 버튼",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //웹뷰 영역
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                Timber.i("WebView shouldOverrideUrlLoading() url: ${request?.url}")

                                //웹 화면에서 이메일 전송 버튼 클릭 -> 기본 이메일 앱 실행
                                if (request?.url.toString().contains("mailto:")) {
                                    val email = request?.url.toString().substringAfter("mailto:")
                                    val emailList = arrayOf(email)
                                    onSendEmailClick(emailList)
                                    return true
                                }

                                return false
                            }

                            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                Timber.i("WebView onPageStarted() url: $url")
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                Timber.i("WebView onPageFinished() url: $url")
                            }

                            /**
                             * This method is called when an error occurs while loading resources (e.g., network issues).
                             */
                            /**
                             * This method is called when an error occurs while loading resources (e.g., network issues).
                             */
                            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                                super.onReceivedError(view, request, error)
                                Timber.e("onReceivedError(): ${request?.url}, error: ${error?.errorCode}, ${error?.description}")
                            }
                            /**
                             * This method is called when the WebView receives an HTTP error (e.g., 404 or 500 status codes).
                             */
                            /**
                             * This method is called when the WebView receives an HTTP error (e.g., 404 or 500 status codes).
                             */
                            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                                super.onReceivedHttpError(view, request, errorResponse)
                            }
                        }
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                Timber.d("onProgressChanged() $newProgress")
                                //onProgress(newProgress)
                                progress = newProgress
                            }
                        }
                        loadUrl(url)
                    }
                }
            )

            if (progress in 1..99) {
                Timber.e("testLog : $progress")
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .align(Alignment.TopCenter),
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
private fun PreviewWebViewScreen() {
    CustomWebViewTheme {
        WebViewScreen(
            title = null,
            url = "",
            onCloseClick = {},
            onSendEmailClick = {}
        )
    }
}