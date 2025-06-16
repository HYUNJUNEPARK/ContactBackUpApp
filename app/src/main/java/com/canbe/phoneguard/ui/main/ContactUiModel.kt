package com.canbe.phoneguard.ui.main

import android.net.Uri

data class ContactUiModel(
    val id: String,
    val name: String,
    val number: List<String>,
    val emails: List<String> = emptyList(),
    val organization: String?,
    val note: String?,
    val profileUri: Uri?
)