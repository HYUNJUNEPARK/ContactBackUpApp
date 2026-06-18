package com.canbe.contactbackup.exception

import android.content.Context
import com.canbe.contactbackup.R

class NoContactsInDevice: Exception()

class ExportFileException(msg: String?): Exception(msg)

class ExtractDataFromFileException(msg: String?): Exception(msg)

class SaveContactException(msg: String?): Exception(msg)

class GetContactsException(msg: String?): Exception(msg)

class UnknownException(msg: String? = null): Exception(msg)

fun convertToErrorMessage(context: Context, e: Exception?): String {
    return when(e) {
        is NoContactsInDevice -> context.getString(R.string.error_no_contacts)
        is UnknownException -> context.getString(R.string.error_unknown)
        is ExportFileException -> context.getString(R.string.error_export_file)
        is ExtractDataFromFileException -> context.getString(R.string.error_extract_data)
        is GetContactsException -> context.getString(R.string.error_get_contacts)
        else -> context.getString(R.string.error_unexpected)
    }
}
