package com.canbe.phoneguard.ui.model

import android.net.Uri
import com.canbe.phoneguard.data.contact.ContactDto

data class ContactUiModel(
    val id: String,
    val name: String,
    val numbers: List<String>,
    val emails: List<String> = emptyList(),
    val organization: String?,
    val note: String?,
    val profileUri: Uri?
)

fun ContactUiModel.toDto(): ContactDto {
    return ContactDto(
        contactId = id,
        displayNamePrimary = name,
        phoneNumbers = numbers,
        emailAddresses = emails,
        organizationCompany = organization,
        notes = note,
        contactPhotoUri = null
    )
}