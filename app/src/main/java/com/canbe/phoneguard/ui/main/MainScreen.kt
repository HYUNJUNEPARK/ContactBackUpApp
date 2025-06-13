package com.canbe.phoneguard.ui.main


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.canbe.phoneguard.R
import com.canbe.phoneguard.domain.contact.model.Contact
import com.canbe.phoneguard.ui.theme.AppSubTheme
import com.canbe.phoneguard.ui.theme.ButtonDefaultColors
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme
import timber.log.Timber


@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToSetting: () -> Unit
) {
    val contactList = viewModel.contactList

    MainScreenContent(
        contactList = contactList.value,
        onNavigateToSetting = onNavigateToSetting,
        onPermissionGranted = { viewModel.getContacts() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    contactList: List<Contact>,
    onNavigateToSetting: () -> Unit,
    onPermissionGranted: () -> Unit
) {
    val permission = Manifest.permission.READ_CONTACTS
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior() //TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val context = LocalContext.current

    var isPermissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        isPermissionGranted = isGranted
        Timber.d("LauncherForActivityResult() isPermissionGranted($isGranted)")
        onPermissionGranted()
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
                    Spacer(modifier = Modifier.size(48.dp)) //아이콘 크기
                },
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.app_name)
                        )

                        if (isPermissionGranted) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                text = "${contactList.size}개의 연락처가 저장되어 있습니다.",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSetting) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "설정 버튼",
                            tint = AppSubTheme
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

                    Column {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(contactList) { contact ->
                                ContactItem(contact)
                            }
                        }
                    }
                } else {
                    //권한이 없는 경우
                    Text("연락처 접근 권한을 허용해 주세요.")

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
fun ContactItem(contact: Contact) {
    Card(
        modifier = Modifier
            .heightIn(min = 120.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = contact.name, style = MaterialTheme.typography.titleMedium)
            Text(text = contact.number[0], style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PhoneGuardTheme {
        MainScreenContent(
            contactList = listOf(
                Contact("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", ""),
                Contact("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", ""),
                Contact("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", ""),
                Contact("1", "12", listOf("01010100101"), listOf("audzxcv"), "asdf", ""),
            ),
            onNavigateToSetting = {},
            onPermissionGranted = {}
        )
    }
}