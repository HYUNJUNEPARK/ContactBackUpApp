package com.canbe.phoneguard.ui.theme

import android.annotation.SuppressLint
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
        containerColor = Color.Gray,         // 배경색
        contentColor = Color.White,         // 텍스트 아이콘 등 콘텐츠 색
        disabledContainerColor = Color.Gray,
        disabledContentColor = Color.DarkGray
    )
}

val backColors = listOf(
    Color(0xFFFFC1CC), // Pastel Pink
    Color(0xFFFFF4B1), // Pastel Yellow
    Color(0xFFAEDFF7), // Pastel Blue
    Color(0xFFB5EAD7), // Pastel Green
    Color(0xFFE2C9F5), // Pastel Purple
    Color(0xFFFFD8B1), // Pastel Orange
    Color(0xFFD7F9F1), // Pastel Mint
    Color(0xFFFCE2CE), // Pastel Peach
    Color(0xFFF8D5EC), // Pastel Rose
    Color(0xFFE4F0D0)  // Pastel Lime
)