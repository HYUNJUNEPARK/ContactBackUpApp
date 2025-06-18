package com.canbe.phoneguard.domain.model

import android.net.Uri
import com.canbe.phoneguard.ui.model.ContactUiModel

data class ContactEntity(
    val id: String,
    val name: String,
    val numbers: List<String>,
    val emails: List<String>,
    val organization: String?,
    val note: String?,
    val profileUri: Uri?
)

fun ContactEntity.toUiModel(): ContactUiModel {
    return ContactUiModel(
        id = id,
        name = name,
        numbers = numbers,
        emails = emails,
        organization = organization,
        note = note,
        profileUri = profileUri
    )
}