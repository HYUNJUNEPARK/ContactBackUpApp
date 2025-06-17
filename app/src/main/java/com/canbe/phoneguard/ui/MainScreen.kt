package com.canbe.phoneguard.ui


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.rememberAsyncImagePainter
import com.canbe.phoneguard.R
import com.canbe.phoneguard.ui.dialog.CustomDialog
import com.canbe.phoneguard.ui.model.ContactUiModel
import com.canbe.phoneguard.ui.model.DialogEvent
import com.canbe.phoneguard.ui.model.UiEvent
import com.canbe.phoneguard.ui.model.UiState
import com.canbe.phoneguard.ui.theme.AppTheme
import com.canbe.phoneguard.ui.theme.ButtonDefaultColors
import com.canbe.phoneguard.ui.theme.FixedTextStyle
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme
import com.canbe.phoneguard.ui.theme.backColors
import timber.log.Timber

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToSetting: () -> Unit,
) {
    val contactList = viewModel.contactList

    val uiState by viewModel.uiState

    var showDialog: DialogEvent? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when(uiEvent) {
                is UiEvent.ShowSuccessDialog -> showDialog = uiEvent.event
                else -> { Timber.e("Not handling this uiEvent $uiEvent") }
            }
        }
    }

    when(showDialog) {
        DialogEvent.EXPORT -> {
            CustomDialog(
                content = "연락처 정보가 파일로 성공적으로 저장되었습니다.\n파일은 [다운로드] 폴더에서 확인할 수 있습니다.",
                isSingleButton = false,
                leftButtonText = "닫기",
                onLeftButtonRequest = { showDialog = null },
                rightButtonText = "파일 확인하기",
                onRightButtonRequest = {},
                onDismissRequest = { showDialog = null }
            )
        }
        else -> {}
    }

    MainScreenContent(
        contactList = contactList.value,
        uiState = uiState,
        onNavigateToSetting = onNavigateToSetting,
        onPermissionGranted = { viewModel.getContacts() },
        onDownloadFileClick = { viewModel.exportToFile() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    contactList: List<ContactUiModel>,
    uiState: UiState,
    onNavigateToSetting: () -> Unit,
    onPermissionGranted: () -> Unit,
    onDownloadFileClick: () -> Unit
) {
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var isPermissionGranted by remember { mutableStateOf(false) }
    val permission = Manifest.permission.READ_CONTACTS

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        isPermissionGranted = isGranted
        Timber.d("LauncherForActivityResult() isPermissionGranted($isGranted)")
        if (isGranted) onPermissionGranted()
    }

    //최초 1회만 권한 확인
    LaunchedEffect(Unit) {
        //권한 확인
        isPermissionGranted = (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)

        if (isPermissionGranted) {
            //권한이 허용 되었을 때
            Timber.d("LaunchedEffect(), 권한 허용")
            onPermissionGranted()
        } else {
            //권한 허용이 되지 않았을 때, 권한 요청
            Timber.d("LaunchedEffect(), 권한 미허용 -> 권한 요청")
            permissionLauncher.launch(permission)
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
                            text = stringResource(R.string.app_name)
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

                        FloatingActionButton(
                            modifier = Modifier
                                .padding(18.dp)
                                .align(Alignment.BottomEnd),
                            onClick = { onDownloadFileClick() },
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
fun ContactItem(contactUiModel: ContactUiModel) {
    val color = remember { backColors.random() }

    Row(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (contactUiModel.profileUri != null) {
            Image(
                painter = rememberAsyncImagePainter(contactUiModel.profileUri),
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .padding(8.dp)
                    .size(38.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "기본 프로필 아이콘",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp)
        ) {
            Text(text = contactUiModel.name, fontSize = 18.sp)
            Text(text = contactUiModel.numbers[0], fontSize = 12.sp)
            HorizontalDivider(thickness = 0.2.dp, color = Color.Gray)
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
            onPermissionGranted = {},
            onDownloadFileClick = {}
        )
    }
}