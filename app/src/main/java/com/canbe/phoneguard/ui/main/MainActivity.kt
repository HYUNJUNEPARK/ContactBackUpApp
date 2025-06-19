package com.canbe.phoneguard.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.canbe.phoneguard.ui.extract.ExtractFileDataActivity
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme
import dagger.hilt.android.AndroidEntryPoint

enum class MainScreenType {
    MAIN, SETTING,
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            PhoneGuardTheme {
                MyApp(this@MainActivity)
            }
        }
    }
}

@Composable
fun MyApp(activity: Activity) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainScreenType.MAIN.name) {
        composable(MainScreenType.MAIN.name) {
            MainScreen(
                onNavigateToSetting = { navController.navigate(MainScreenType.SETTING.name) },
                onGoToExtractFileDataActivity = {
                    val intent = Intent(activity, ExtractFileDataActivity::class.java)
                    activity.startActivity(intent)
                }
            )
        }
        composable(MainScreenType.SETTING.name) {
            SettingScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}