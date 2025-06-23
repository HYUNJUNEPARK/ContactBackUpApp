package com.canbe.contactbackup.exception

class NoContactsInDevice: Exception()

fun convertToErrorMessage(e: Exception?): String {
    return when(e) {
        is NoContactsInDevice -> "저장된 연락처가 없습니다."
        else -> "알 수 없는 에러가 발생했습니다."
    }
}