package com.canbe.contactbackup.ui.setting

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.canbe.contactbackup.ui.base.BaseActivity
import com.canbe.contactbackup.ui.theme.ContactBackupTheme

class SettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactBackupTheme {
                SettingScreen(
                    onBack = { this.finish() }
                )
            }
        }
    }
}