package com.canbe.defaultwebview

import android.content.Context
import android.content.Intent

object WebViewer {
    const val WEB_VIEW_URL_INTENT_KEY = "module.webview.url"
    const val WEB_VIEW_TITLE_INTENT_KEY = "module.webview.title"

    fun startWebView(
        context: Context,
        url: String,
        webViewTitle: String = ""
    ) {
        val intent = Intent(context, WebViewActivity::class.java).apply {
            putExtra(WEB_VIEW_URL_INTENT_KEY, url)
            putExtra(WEB_VIEW_TITLE_INTENT_KEY, webViewTitle)
        }
        context.startActivity(intent)
    }
}