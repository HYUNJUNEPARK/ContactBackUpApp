package com.canbe.contactbackup.ui.extract

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canbe.contactbackup.R
import com.canbe.contactbackup.ui.model.ContactUiModel
import com.canbe.contactbackup.ui.model.UiState
import com.canbe.contactbackup.ui.theme.ContactItem
import com.canbe.contactbackup.ui.theme.CustomDefaultButton
import com.canbe.contactbackup.ui.theme.FixedTextStyle
import com.canbe.contactbackup.ui.theme.Mint
import com.canbe.contactbackup.ui.theme.ContactBackupTheme

@Composable
fun ExtractFileDataScreen(
    viewModel: ExtractFileDataViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val contactList = viewModel.contactList
    val uiState by viewModel.uiState

    ExtractFileDataScreenContent(
        onBack = onBack,
        contactList = contactList.value,
        uiState = uiState,
        onGetFileData = { viewModel.extractFromFile(it) },
        onBackUpButtonClick = { viewModel.saveContactsToDevice() }
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
                    Text(text = stringResource(R.string.restore_contact), fontSize = 16.sp)
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
                    CustomDefaultButton(
                        text = stringResource(R.string.select_contact_file),
                        buttonColor = Mint
                    ) {
                        launcher.launch(arrayOf("application/json"))
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        //복원 가능한 연락처가 있는 경우
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(contactList) { contact ->
                                ContactItem(contact) { //contactUiModel ->

                                }
                            }
                        }

                        FloatingActionButton(
                            modifier = Modifier
                                .padding(18.dp)
                                .size(68.dp)
                                .align(Alignment.BottomEnd),
                            containerColor = Mint,
                            onClick = { onBackUpButtonClick() }
                        ) {
                            Column(
                                Modifier.padding(3.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_outline_file_upload_24),
                                    contentDescription = "FAB Button: Export File",
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 3.dp),
                                    style = FixedTextStyle(8.sp),
                                    textAlign = TextAlign.Center,
                                    text = stringResource(R.string.restore_contact),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
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
    ContactBackupTheme {
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