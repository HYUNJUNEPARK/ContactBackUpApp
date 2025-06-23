package com.canbe.contactbackup.ui.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canbe.contactbackup.BuildConfig
import com.canbe.contactbackup.R
import com.canbe.contactbackup.ui.theme.ContactBackupTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(onBack: () -> Unit) {
    val appVersion = if (BuildConfig.DEBUG) {
        "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
    } else {
        BuildConfig.VERSION_NAME
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.setting), fontSize = 16.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "뒤로가기",
                        )
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
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = "개인 정보 처리 방침"
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp)
                ) {
                    Text(text = "앱 버전")

                    Spacer(modifier = Modifier.weight(1f))

                    Text(text = appVersion)
                }

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    ContactBackupTheme {
        SettingScreen({})
    }
}