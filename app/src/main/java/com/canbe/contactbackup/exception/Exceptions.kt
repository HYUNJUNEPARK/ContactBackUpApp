package com.canbe.contactbackup.exception

class NoContactsInDevice: Exception()

class ExportFileException(msg: String?): Exception(msg)

class ExtractDataFromFileException(msg: String?): Exception(msg)

class SaveContactException(msg: String?): Exception(msg)

class GetContactsException(msg: String?): Exception(msg)

class UnknownException(msg: String? = null): Exception(msg)

fun convertToErrorMessage(e: Exception?): String {
    return when(e) {
        is NoContactsInDevice -> "저장된 연락처가 없습니다."
        is UnknownException -> "알 수 없는 에러가 발생했습니다.(Unknown Throwable)"
        is ExportFileException -> "파일을 생성하지 못했습니다."
        is ExtractDataFromFileException -> "파일에서 연락처 정보를 불러오지 못했습니다."
        is GetContactsException -> "연락처를 불러오는 데 실패했습니다."
        else -> "예기치 않은 오류가 발생했습니다."
    }
}