package com.canbe.contactbackup.domain.model

import android.net.Uri
import com.canbe.contactbackup.data.model.ContactDto

data class ContactEntity(
    val id: String,
    val name: String,
    val numbers: List<String>,
    val emails: List<String>,
    val organization: String?,
    val note: String?,
    val profileUri: Uri?
)

fun ContactEntity.toDto(): ContactDto {
    return ContactDto(
        contactId = id,
        displayNamePrimary = name,
        phoneNumbers = numbers,
        emailAddresses = emails,
        organizationCompany = organization,
        notes = note,
        contactPhotoUri = profileUri?.toString()
    )
}
