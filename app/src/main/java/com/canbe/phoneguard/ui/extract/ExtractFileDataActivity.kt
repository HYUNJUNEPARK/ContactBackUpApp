package com.canbe.phoneguard.ui.extract

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.canbe.phoneguard.ui.base.BaseActivity
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtractFileDataActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhoneGuardTheme {
                ExtractFileDataScreen (
                    onBack = { this.finish() }
                )
            }
        }
    }
}
