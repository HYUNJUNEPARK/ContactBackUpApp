package com.canbe.phoneguard.ui.extract

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtractFileDataActivity : ComponentActivity() {
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
