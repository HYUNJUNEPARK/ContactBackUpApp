package com.canbe.phoneguard.data.contact.model

import android.net.Uri

data class ContactEntity(
    val contactId: String,
    val displayNamePrimary: String?,
    val phoneNumbers: List<String>,
    val emailAddresses: List<String>,
    val organizationCompany: String?,
    val notes: String?,
    val contactPhotoUri: Uri?
)
