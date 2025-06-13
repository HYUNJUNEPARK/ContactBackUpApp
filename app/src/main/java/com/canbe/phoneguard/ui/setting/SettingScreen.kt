package com.canbe.phoneguard.ui.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.canbe.phoneguard.R
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.setting),
                        fontSize = 16.sp
                    ) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("This is the Setting Screen")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    PhoneGuardTheme {
        SettingScreen({})
    }
}