package com.canbe.phoneguard.ui.main


import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
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
import com.canbe.phoneguard.data.contact.ContactDto
import com.canbe.phoneguard.ui.theme.AppTheme
import com.canbe.phoneguard.ui.theme.ButtonDefaultColors
import com.canbe.phoneguard.ui.theme.FixedTextStyle
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme
import com.canbe.phoneguard.ui.theme.backColors
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToSetting: () -> Unit,
) {
    val context = LocalContext.current

    val contactList = viewModel.contactList
    val isLoading = viewModel.isLoading

    MainScreenContent(
        contactList = contactList.value,
        isLoading = isLoading,
        onNavigateToSetting = onNavigateToSetting,
        onPermissionGranted = { viewModel.getContacts() },
        onDownloadFileClick = {
            //saveToDownloads(context, contactList = contactList.value)
            val tt = loadFromDownloads()
            Timber.e("testLog $tt")
        }
    )
}




private const val FILE_NAME = "user_list.json"

/**
 * JSON 파일 저장
 */
fun saveToDownloads(
    context: Context,
    fileName: String = FILE_NAME,
    directory: String = Environment.DIRECTORY_DOWNLOADS,
    contactList: List<ContactUiModel>
) {
    Timber.d("saveToDownloads() $contactList")

    //UIModel -> DTO -> JSON 문자열 직렬화
    val mContactList = contactList.map { it.toDto() }
    val jsonString = Gson().toJson(mContactList)

    /*
        API 29 이상 : MediaStore 를 사용하여 Download 폴더에 접근
        API 29 미만 : 직접 Download 폴더 경로에 파일 생성
     */
    val outputStream: OutputStream? = if (Build.VERSION.SDK_INT >= 29) {
        //API 29 이상
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/json")
            put(MediaStore.Downloads.RELATIVE_PATH, directory) // 저장 경로 (Downloads)
            put(MediaStore.Downloads.IS_PENDING, 1)  // 저장 중 상태 표시
        }

        val resolver = context.contentResolver
        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val itemUri: Uri? = resolver.insert(collection, contentValues)

        // outputStream 을 열고, 저장 완료 후 IS_PENDING 을 0으로 변경
        itemUri?.let { uri ->
            resolver.openOutputStream(uri)?.also {
                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0) //저장 완료 상태 표시 -> 0 으로 바꿔줘야 다른 앱에서 접근이 가능
                resolver.update(uri, contentValues, null, null)
            }
        }
    } else {
        //API 29 미만
        val downloadsDir = Environment.getExternalStoragePublicDirectory(directory)
        val file = File(downloadsDir, fileName)
        FileOutputStream(file)
    }

    try {
        // OutputStream 에 JSON 문자열을 바이트 배열로 변환하여 저장
        // 해당 코드를 실행시키지 않는다면, 실제 파일에는 아무런 데이터도 저장되지 않는다.
        outputStream?.use {
            it.write(jsonString.toByteArray())
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun loadFromDownloads(): List<ContactDto> {
    Timber.d("loadFromDownloads()")

    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FILE_NAME)
    if (!file.exists()) return emptyList()

    return try {
        val jsonString = file.readText()
        val listType = object : TypeToken<List<ContactDto>>() {}.type
        Gson().fromJson(jsonString, listType)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}









@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    contactList: List<ContactUiModel>,
    isLoading: Boolean,
    onNavigateToSetting: () -> Unit,
    onPermissionGranted: () -> Unit,
    onDownloadFileClick: () -> Unit
) {
    val permission = Manifest.permission.READ_CONTACTS
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val context = LocalContext.current

    var isPermissionGranted by remember { mutableStateOf(false) }

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
                    Spacer(modifier = Modifier.size(48.dp)) //아이콘 크기
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
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "더보기 버튼",
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
                        if (isLoading) {
                            CircularProgressIndicator(color = colorResource(R.color.appTheme))
                        } else {
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
            isLoading = false,
            onNavigateToSetting = {},
            onPermissionGranted = {},
            onDownloadFileClick = {}
        )
    }
}