package com.canbe.phoneguard.domain.contact.model

import android.net.Uri
import com.canbe.phoneguard.ui.main.ContactUiModel

data class ContactEntity(
    val id: String,
    val name: String,
    val number: List<String>,
    val emails: List<String>,
    val organization: String?,
    val note: String?,
    val profileUri: Uri?
)

fun ContactEntity.toUiModel(): ContactUiModel {
    return ContactUiModel(
        id = id,
        name = name,
        number = number,
        emails = emails,
        organization = organization,
        note = note,
        profileUri = profileUri
    )
}