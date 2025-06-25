package com.canbe.contactbackup.ui.model

import android.net.Uri
import com.canbe.contactbackup.domain.model.ContactEntity

data class ContactUiModel(
    val id: String,
    val name: String,
    val numbers: List<String>,
    val emails: List<String> = emptyList(),
    val organization: String?,
    val note: String?,
    val profileUri: Uri?
)

fun ContactUiModel.toEntity(): ContactEntity {
    return ContactEntity(
        id = id,
        name = name,
        numbers = numbers,
        emails = emails,
        organization = organization,
        note = note,
        profileUri = profileUri
    )
}