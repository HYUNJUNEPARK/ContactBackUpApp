package com.canbe.phoneguard.ui.main


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.canbe.phoneguard.R
import com.canbe.phoneguard.ui.dialog.CustomDefaultDialog
import com.canbe.phoneguard.ui.model.ContactUiModel
import com.canbe.phoneguard.ui.model.EventType
import com.canbe.phoneguard.ui.model.UiEvent
import com.canbe.phoneguard.ui.model.UiState
import com.canbe.phoneguard.ui.theme.ButtonDefaultColors
import com.canbe.phoneguard.ui.theme.ContactItem
import com.canbe.phoneguard.ui.theme.FixedTextStyle
import com.canbe.phoneguard.ui.theme.Mint
import com.canbe.phoneguard.ui.theme.Orange
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme
import com.canbe.phoneguard.ui.theme.PurpleLight
import timber.log.Timber

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToSetting: () -> Unit,
    onGoToExtractFileDataActivity: () -> Unit
) {
    val context = LocalContext.current

    val contactList = viewModel.contactList

    val uiState by viewModel.uiState

    var pendingUiEvent by remember { mutableStateOf<UiEvent?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when(uiEvent) {
                is UiEvent.ShowDialog -> { pendingUiEvent = uiEvent }
                is UiEvent.ShowToast -> { Toast.makeText(context, uiEvent.message, Toast.LENGTH_SHORT).show() }
            }
        }
    }

    //UiEvent Handler
    pendingUiEvent?.let { event ->
        when (event) {
            is UiEvent.ShowDialog -> {
                when (event.eventType) {
                    EventType.EXPORT -> {
                        CustomDefaultDialog(
                            title = stringResource(R.string.export_file_2),
                            content = "연락처를 파일로 저장합니다.\n생성된 파일은 [다운로드] 폴더에서 확인하세요.",
                            leftButtonText = stringResource(R.string.close),
                            onLeftButtonRequest = { pendingUiEvent = null },
                            rightButtonText = stringResource(R.string.export_file_2),
                            onRightButtonRequest = {
                                val filename = "PHONE_NUMBER_BACKUP_${System.currentTimeMillis()}.json"
                                viewModel.exportToFile(filename)
                            },
                            onDismissRequest = { pendingUiEvent = null },
                        )
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }

    MainScreenContent(
        contactList = contactList.value,
        uiState = uiState,
        onNavigateToSetting = onNavigateToSetting,
        onPermissionGranted = { viewModel.getContacts() },
        onExportFileClick = { viewModel.updateUiEvent(UiEvent.ShowDialog(EventType.EXPORT)) },
        onGoToExtractFileDataActivity = onGoToExtractFileDataActivity
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    contactList: List<ContactUiModel>,
    uiState: UiState,
    onNavigateToSetting: () -> Unit,
    onPermissionGranted: () -> Unit,
    onExportFileClick: () -> Unit,
    onGoToExtractFileDataActivity: () -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var isPermissionGranted by remember { mutableStateOf(false) }

    val permission = listOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val grantedPermissions = permissions.filter { it.value }
        val notGrantedPermissions = permissions.filter { !it.value }
        Timber.d("grantedPermissions($grantedPermissions), notGrantedPermissions($notGrantedPermissions)")

        //메인 화면에서 꼭 필요한 권한만 있으면 동작
        isPermissionGranted = grantedPermissions.contains(Manifest.permission.READ_CONTACTS)

        if (isPermissionGranted) onPermissionGranted()
    }

    //최초 1회만 권한 확인
    LaunchedEffect(Unit) {
        //연락처 읽기 권한 확인
        isPermissionGranted = (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)

        if (isPermissionGranted) {
            //권한이 허용 되었을 때
            Timber.d("LaunchedEffect(), 권한 허용")
            onPermissionGranted()
        } else {
            //권한 허용이 되지 않았을 때, 권한 요청
            Timber.d("LaunchedEffect(), 권한 미허용 -> 권한 요청")
            permissionLauncher.launch(permission.toTypedArray())
        }
    }

    //UI
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Spacer(modifier = Modifier.size(48.dp))
                },
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 3.dp),
                            style = FixedTextStyle(16.sp),
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.contact)
                        )

                        if (isPermissionGranted) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                style = FixedTextStyle(),
                                textAlign = TextAlign.Center,
                                text = stringResource(R.string.format_contact_count, contactList.size),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSetting) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "설정 버튼",
                            tint = colorResource(R.color.gray)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
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
                if (isPermissionGranted) {
                    //연락처 접근 권한이 허용된 경우
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when(uiState) {
                            is UiState.Loading -> CircularProgressIndicator(color = colorResource(R.color.appTheme))
                            is UiState.Success -> {}
                            is UiState.Error -> {
                                Timber.e("Exception: ${uiState.exception}")
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(contactList) { contact ->
                                ContactItem(contact)
                            }
                        }

                        FabButton(
                            onExportButtonClick = { onExportFileClick() },
                            onExtractFileDataButtonClick = { onGoToExtractFileDataActivity() }
                        )
                    }
                } else {
                    //권한이 없는 경우
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.msg_allow_contact)
                    )

                    Button(
                        modifier = Modifier.padding(top = 24.dp),
                        colors = ButtonDefaultColors(),
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.go_to_setting),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun FabButton(
    onExportButtonClick: () -> Unit,
    onExtractFileDataButtonClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        //버튼 1
        AnimatedVisibility(
            visible = expanded, //expanded true 인 경우만 보임
            enter = fadeIn() + slideInVertically { it }, //아래 -> 위 등장
            exit = fadeOut() + slideOutVertically { it } //위 -> 아래 사라짐
        ) {
            FloatingActionButton(
                onClick = { onExportButtonClick() },
                modifier = Modifier
                    .padding(bottom = 170.dp)
                    .size(68.dp),
                containerColor = PurpleLight
            ) {
                Column(
                    Modifier.padding(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_outline_file_download_24),
                        contentDescription = "FAB Button: Export File",
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 3.dp),
                        style = FixedTextStyle(8.sp),
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.export_file_1),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 버튼 2
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 }
        ) {
            FloatingActionButton(
                onClick = { onExtractFileDataButtonClick() },
                modifier = Modifier
                    .padding(bottom = 90.dp)
                    .size(68.dp),
                containerColor = Mint
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

        // 메인 FAB 버튼
        FloatingActionButton(
            modifier = Modifier.size(68.dp),
            onClick = { expanded = !expanded },
            containerColor = Orange
        ) {
            Image(
                painter = painterResource(R.drawable.ic_import_export_24),
                contentDescription = "FAB Button: Export File",
                modifier = Modifier.size(42.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PhoneGuardTheme {
        MainScreenContent(
            contactList = listOf(
                ContactUiModel("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", "", null),
                ContactUiModel("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", "", null),
                ContactUiModel("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", "", null),
                ContactUiModel("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", "", null),
            ),
            uiState = UiState.Loading,
            onNavigateToSetting = {},
            onGoToExtractFileDataActivity = {},
            onPermissionGranted = {},
            onExportFileClick = {}
        )
    }
}
