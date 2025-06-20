package com.canbe.contactbackup.ui.extract

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.canbe.contactbackup.ui.base.BaseActivity
import com.canbe.contactbackup.ui.theme.ContactBackupTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtractFileDataActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactBackupTheme {
                ExtractFileDataScreen (
                    onBack = { this.finish() }
                )
            }
        }
    }
}
