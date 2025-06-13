package com.canbe.phoneguard.data.contact

import android.content.ContentResolver
import android.provider.ContactsContract
import timber.log.Timber
import javax.inject.Inject

class ContactDataSource @Inject constructor(
    private val contentResolver: ContentResolver
) {
    fun getContactList(): List<ContactEntity> {
        Timber.i("getContacts()")
        val contactList = mutableListOf<ContactEntity>()

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

                val phones = mutableListOf<String>()
                val emails = mutableListOf<String>()
                var organization: String? = null
                var note: String? = null

                // 전화번호
                val phoneCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(id),
                    null
                )
                phoneCursor?.use { pc ->
                    while (pc.moveToNext()) {
                        val phoneNumber = pc.getString(pc.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        phones.add(phoneNumber)
                    }
                }

                // 이메일
                val emailCursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
                    arrayOf(id),
                    null
                )
                emailCursor?.use { ec ->
                    while (ec.moveToNext()) {
                        val email = ec.getString(ec.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS))
                        emails.add(email)
                    }
                }

                // 기타 상세 정보 (조직, 메모 등)
                val dataCursor = contentResolver.query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    "${ContactsContract.Data.CONTACT_ID} = ?",
                    arrayOf(id),
                    null
                )
                dataCursor?.use { dc ->
                    while (dc.moveToNext()) {
                        val mimeType = dc.getString(dc.getColumnIndexOrThrow(ContactsContract.Data.MIMETYPE))
                        when (mimeType) {
                            //조직
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE -> {
                                organization = dc.getString(dc.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.COMPANY))
                            }
                            //메모
                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE -> {
                                note = dc.getString(dc.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Note.NOTE))
                            }
                        }
                    }
                }

                //전화 번호가 1개 이상인 유효한 데이터만 가져온다.
                if (phones.size > 0) {
                    contactList.add(
                        ContactEntity(
                            contactId = id,
                            displayNamePrimary = name,
                            phoneNumbers = phones,
                            emailAddresses = emails,
                            organizationCompany = organization,
                            notes = note
                        )
                    )
                }
            }
        }

        Timber.i("getContacts(): $contactList")
        return contactList
    }

}