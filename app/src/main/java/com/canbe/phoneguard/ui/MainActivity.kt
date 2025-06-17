package com.canbe.phoneguard.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.canbe.phoneguard.ui.screen.ExtractFileDataScreen
import com.canbe.phoneguard.ui.screen.MainScreen
import com.canbe.phoneguard.ui.screen.SettingScreen
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme
import dagger.hilt.android.AndroidEntryPoint

enum class MainScreen {
    MAIN, SETTING, EXTRACT
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhoneGuardTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainScreen.MAIN.name) {
        composable(MainScreen.MAIN.name) {
            MainScreen(
                onNavigateToSetting = { navController.navigate(MainScreen.SETTING.name) },
                onNavigateToExtractContent = { navController.navigate(MainScreen.EXTRACT.name) }
            )
        }
        composable(MainScreen.SETTING.name) {
            SettingScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(MainScreen.EXTRACT.name) {
            ExtractFileDataScreen (
                onBack = { navController.popBackStack() }
            )
        }
    }
}