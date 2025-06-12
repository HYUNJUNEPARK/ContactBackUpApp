package com.canbe.phoneguard.data.contact

import android.content.Context
import android.provider.ContactsContract
import com.canbe.phoneguard.domain.contact.ContactRepository
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor() : ContactRepository {
    override fun getContacts(context: Context): List<String> {
        val result = mutableListOf<String>()
        val resolver = context.contentResolver

        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            null
        )

        cursor?.use {
            val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIdx)
                val number = it.getString(numberIdx)
                result.add("$name: $number")
            }
        }

        return result
    }
}