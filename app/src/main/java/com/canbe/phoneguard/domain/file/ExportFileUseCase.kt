package com.canbe.phoneguard.domain.file

import com.canbe.phoneguard.ui.model.ContactUiModel
import com.canbe.phoneguard.ui.model.toDto
import com.google.gson.Gson
import javax.inject.Inject

class ExportFileUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend operator fun invoke(
        fileName: String,
        contactList: List<ContactUiModel>
    ) {
        //UIModel -> DTO -> JSON 문자열 직렬화
        val mContactList = contactList.map { it.toDto() }
        val fileContent = Gson().toJson(mContactList)
        repository.exportToFile(fileName, fileContent)
    }
}