package com.canbe.contactbackup.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.canbe.contactbackup.ui.base.BaseActivity
import com.canbe.contactbackup.ui.extract.ExtractFileDataActivity
import com.canbe.contactbackup.ui.setting.SettingActivity
import com.canbe.contactbackup.ui.theme.ContactBackupTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactBackupTheme {
                MyApp(this@MainActivity)
            }
        }
    }
}

@Composable
fun MyApp(
    activity: Activity,
    viewModel: MainViewModel = hiltViewModel()
) {
    MainScreen(
        viewModel = viewModel,
        onStartSettingActivity = {
            val intent = Intent(activity, SettingActivity::class.java)
            activity.startActivity(intent)
        },
        onStartExtractFileDataActivity = {
            val intent = Intent(activity, ExtractFileDataActivity::class.java)
            activity.startActivity(intent)
        },
        onNavigationToContactDetail = { //contact ->
            //TODO 출시 후 기능 추가
        }
    )
}
