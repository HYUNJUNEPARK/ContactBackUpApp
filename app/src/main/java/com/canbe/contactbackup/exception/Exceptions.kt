package com.canbe.contactbackup.exception

class NoContactsInDevice: Exception()

class UnknownException(msg: String? = null): Exception(msg)

fun convertToErrorMessage(e: Exception?): String {
    return when(e) {
        is NoContactsInDevice -> "저장된 연락처가 없습니다."
        is UnknownException -> "알 수 없는 에러가 발생했습니다.(Unknown Throwable)"
        else -> "알 수 없는 에러가 발생했습니다."
    }
}