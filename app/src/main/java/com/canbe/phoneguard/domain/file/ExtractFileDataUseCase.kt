package com.canbe.phoneguard.domain.file

import android.net.Uri
import com.canbe.phoneguard.data.model.toEntity
import com.canbe.phoneguard.domain.contact.toUiModel
import com.canbe.phoneguard.ui.model.ContactUiModel
import javax.inject.Inject

class ExtractFileDataUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend operator fun invoke(uri: Uri): List<ContactUiModel> {
        val fileContactList = repository.extractDataFromFile(uri)
        return fileContactList.map { it.toEntity() }.map { it.toUiModel() }
    }
}