package com.canbe.contactbackup.application

import android.app.Application
import com.canbe.contactbackup.BuildConfig
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(CustomDebugTree())
        MobileAds.initialize(this)
    }

    class CustomDebugTree: Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String {
            return "debugLog:${element.fileName}:${element.lineNumber}"
        }
    }
}