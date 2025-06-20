package com.canbe.contactbackup.data.model

import androidx.core.net.toUri
import com.canbe.contactbackup.domain.model.ContactEntity

data class ContactDto(
    val contactId: String,
    val displayNamePrimary: String?,
    val phoneNumbers: List<String>,
    val emailAddresses: List<String>,
    val organizationCompany: String?,
    val notes: String?,
    val contactPhotoUri: String?
)

fun ContactDto.toEntity(): ContactEntity {
    return ContactEntity(
        id = contactId,
        name = displayNamePrimary ?: "",
        numbers = phoneNumbers,
        emails = emailAddresses,
        organization = organizationCompany,
        note = notes,
        profileUri = contactPhotoUri?.toUri()
    )
}