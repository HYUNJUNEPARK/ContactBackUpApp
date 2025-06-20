package com.canbe.contactbackup.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.canbe.contactbackup.ui.theme.ContactBackupTheme
import timber.log.Timber

@Composable
fun ContactDetailScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val selectContact = viewModel.selectedContact
    Timber.e("testLog $selectContact")

    // Screen이 사라질 때 초기화
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectContact()
        }
    }

    ContactDetailScreenContent(
        onBack = onBack,

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreenContent(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "", fontSize = 16.sp)
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
                Text("This is the Setting Screen1") // 상단 고정

                Spacer(modifier = Modifier.weight(1f)) // 가운데 공간 차지

                Text("This is the Setting Screen2") // 하단 고정
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    ContactBackupTheme {
        ContactDetailScreenContent(
            onBack = {}
        )
    }
}