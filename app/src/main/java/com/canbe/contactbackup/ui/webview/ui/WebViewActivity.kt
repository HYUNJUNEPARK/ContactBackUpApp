package com.canbe.contactbackup.ui.webview.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.canbe.contactbackup.ui.base.BaseActivity
import com.canbe.contactbackup.ui.webview.CustomWebView.WEB_VIEW_TITLE_INTENT_KEY
import com.canbe.contactbackup.ui.webview.CustomWebView.WEB_VIEW_URL_INTENT_KEY

class WebViewActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WebViewScreen(
                title = intent.getStringExtra(WEB_VIEW_TITLE_INTENT_KEY),
                url = intent.getStringExtra(WEB_VIEW_URL_INTENT_KEY) ?: "",
                onCloseClick = { finish() },
                onSendEmailClick = { emailList ->
                    startEmailApp(emailList)
                }
            )
        }
    }

    override fun onDestroy() {
        clearWebData(this)
        super.onDestroy()
    }

    /**
     * 쿠키 및 캐시 삭제
     */
    private fun clearWebData(context: Context) {
        // 쿠키 삭제
        CookieManager.getInstance().apply {
            removeAllCookies(null)
            flush()
        }

        // WebView 캐시 삭제
        WebStorage.getInstance().deleteAllData()
        WebView(context).clearCache(true)
    }

    /**
     * 이메일 앱 실행
     */
    private fun startEmailApp(emailList: Array<String>) {
        val emailSelectorIntent = Intent(Intent.ACTION_SENDTO)
        emailSelectorIntent.setData(Uri.parse("mailto:"))

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, emailList)
            putExtra(Intent.EXTRA_TEXT, "")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            selector = emailSelectorIntent
        }

        startActivity(emailIntent)
    }
}