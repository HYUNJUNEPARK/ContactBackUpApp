package com.canbe.phoneguard.ui.theme

import android.annotation.SuppressLint
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AppTheme = Color(0xFF198BD7)
val AppSubTheme = Color(0xFF5497D7)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

@SuppressLint("ComposableNaming")
@Composable
fun ButtonDefaultColors() : ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = AppTheme,         // 배경색
        contentColor = Color.White,         // 텍스트 아이콘 등 콘텐츠 색
        disabledContainerColor = Color.Gray,
        disabledContentColor = Color.DarkGray
    )
}