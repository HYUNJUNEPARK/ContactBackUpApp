package com.canbe.phoneguard.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canbe.phoneguard.R
import com.canbe.phoneguard.ui.MainViewModel
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme


@Composable
fun ExtractFileDataScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    ExtractFileDataScreenContent(
        onBack = onBack,
        onFileUri = { viewModel.extractFromFile(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtractFileDataScreenContent(
    onBack: () -> Unit,
    onFileUri: (Uri) -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let { onFileUri(it) }
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.setting), fontSize = 16.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Image(
                            painter = painterResource(R.drawable.ic_arrow_back_24_day_night),
                             contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
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
                Text("This is the Extract Screen1") // 상단 고정

                Spacer(modifier = Modifier.weight(1f)) // 가운데 공간 차지

                IconButton(onClick = {
                    launcher.launch(arrayOf("application/json"))
                }) {
                    Image(
                        painter = painterResource(R.drawable.ic_arrow_back_24_day_night),
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text("This is the Extract Screen2") // 하단 고정
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ExtractContentScreenPreview() {
    PhoneGuardTheme {
        ExtractFileDataScreenContent(
            onBack = {},
            onFileUri = {}
        )
    }
}