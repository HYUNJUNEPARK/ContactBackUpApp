package com.canbe.phoneguard.ui


import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.canbe.phoneguard.ui.theme.PhoneGuardTheme
import timber.log.Timber


@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToSetting: () -> Unit
) {
    MainScreenContent(onNavigateToSetting = onNavigateToSetting)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    onNavigateToSetting: () -> Unit
) {
    val context = LocalContext.current

    var isPermissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isPermissionGranted = isGranted
        if (isGranted) {
            Timber.e("testLog:1")
        }
    }

    //최초 1회만 권한 확인
    LaunchedEffect(Unit) {
        val permission = Manifest.permission.READ_CONTACTS

        isPermissionGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

        if (!isPermissionGranted) {
            permissionLauncher.launch(permission)
        } else {
            //viewModel.loadContacts(context)
            Timber.e("testLog:2")
        }
    }

    //UI
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Main Screen") })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val tt = if (isPermissionGranted) "TRUE" else "FALSE"

                Text("This is the Main Screen $tt")
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onNavigateToSetting) {
                    Text("Go to Setting")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PhoneGuardTheme {
        MainScreenContent(onNavigateToSetting = {})
    }
}