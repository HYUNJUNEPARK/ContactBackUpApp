package com.canbe.phoneguard.ui.extract

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canbe.phoneguard.R
import com.canbe.phoneguard.ui.model.ContactUiModel
import com.canbe.phoneguard.ui.model.UiState
import com.canbe.phoneguard.ui.theme.AppTheme
import com.canbe.phoneguard.ui.theme.ContactItem
import com.canbe.phoneguard.ui.theme.CustomStyledButton
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme


@Composable
fun ExtractFileDataScreen(
    viewModel: ExtractFileDataViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val contactList = viewModel.contactList

    val uiState by viewModel.uiState

    val context = LocalContext.current

    ExtractFileDataScreenContent(
        onBack = onBack,
        contactList = contactList.value,
        uiState = uiState,
        onGetFileData = { viewModel.extractFromFile(it) },
        onBackUpButtonClick = { viewModel.saveDataToDevice(context) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtractFileDataScreenContent(
    onBack: () -> Unit,
    contactList: List<ContactUiModel>,
    uiState: UiState,
    onGetFileData: (Uri) -> Unit,
    onBackUpButtonClick: () -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let { onGetFileData(it) }
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.contact_backup), fontSize = 16.sp)
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
                if (contactList.isEmpty()) {
                    //복원 가능한 연락처가 없는 경우
                    CustomStyledButton("연락처 파일 가져오기") {
                        launcher.launch(arrayOf("application/json"))
                    }
                } else {

                    Box(modifier = Modifier.fillMaxSize()) {
                        //복원 가능한 연락처가 있는 경우
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(contactList) { contact ->
                                ContactItem(contact)
                            }
                        }

                        FloatingActionButton(
                            modifier = Modifier
                                .padding(18.dp)
                                .align(Alignment.BottomEnd),
                            onClick = { onBackUpButtonClick() },
                            containerColor = AppTheme,
                            contentColor = Color.White,
                            shape = CircleShape
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_file_download_24),
                                contentDescription = "파일 다운로드",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ExtractFileDataScreenPreview() {
    PhoneGuardTheme {
        ExtractFileDataScreenContent(
            onBack = {},
            contactList = listOf(
                ContactUiModel("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", "", null),
                ContactUiModel("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", "", null),
                ContactUiModel("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", "", null),
                ContactUiModel("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", "", null),
            ),
            uiState = UiState.Loading,
            onGetFileData = {},
            onBackUpButtonClick = {}
        )
    }
}