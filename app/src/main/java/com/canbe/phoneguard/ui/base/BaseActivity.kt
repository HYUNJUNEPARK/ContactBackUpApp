package com.canbe.phoneguard.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import timber.log.Timber

open class BaseActivity : ComponentActivity() {
    companion object {
        const val LOG_TAG_LIFE_CYCLE = "debugLog_lifecycle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(LOG_TAG_LIFE_CYCLE).i("${javaClass.simpleName} onCreate()")
    }

    override fun onResume() {
        super.onResume()
        Timber.tag(LOG_TAG_LIFE_CYCLE).i("${javaClass.simpleName} onResume()")
    }

    override fun onPause() {
        super.onPause()
        Timber.tag(LOG_TAG_LIFE_CYCLE).i("${javaClass.simpleName} onPause()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag(LOG_TAG_LIFE_CYCLE).d("${javaClass.simpleName} onDestroy()")
    }
}