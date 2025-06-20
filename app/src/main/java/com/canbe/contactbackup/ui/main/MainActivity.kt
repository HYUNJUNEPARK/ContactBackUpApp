package com.canbe.contactbackup.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.canbe.contactbackup.ui.base.BaseActivity
import com.canbe.contactbackup.ui.extract.ExtractFileDataActivity
import com.canbe.contactbackup.ui.setting.SettingActivity
import com.canbe.contactbackup.ui.theme.ContactBackupTheme
import dagger.hilt.android.AndroidEntryPoint

enum class MainScreenType {
    MAIN, CONTACT_DETAIL,
}

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
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainScreenType.MAIN.name) {
        composable(MainScreenType.MAIN.name) {
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
                onNavigationToContactDetail = { contact ->
                    viewModel.setSelectContact(contact)
                    navController.navigate(MainScreenType.CONTACT_DETAIL.name)
                }
            )
        }
        composable(MainScreenType.CONTACT_DETAIL.name) {
            ContactDetailScreen (
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}