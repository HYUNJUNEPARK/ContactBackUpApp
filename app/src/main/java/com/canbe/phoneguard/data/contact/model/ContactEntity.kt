package com.canbe.phoneguard.data.contact.model

data class ContactEntity(
    val contactId: String,
    val displayNamePrimary: String?,
    val phoneNumbers: List<String>,
    val emailAddresses: List<String>,
    val organizationCompany: String?,
    val notes: String?
)
