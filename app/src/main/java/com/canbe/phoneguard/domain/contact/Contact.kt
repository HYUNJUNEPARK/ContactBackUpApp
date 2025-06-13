package com.canbe.phoneguard.domain.contact

data class Contact(
    val id: String,
    val name: String,
    val number: List<String>,
    val emails: List<String>,
    val organization: String?,
    val note: String?
)
