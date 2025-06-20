package com.canbe.contactbackup.domain.file

import android.net.Uri
import com.canbe.contactbackup.domain.model.toUiModel
import com.canbe.contactbackup.ui.model.ContactUiModel
import javax.inject.Inject

class ExtractFileDataUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend operator fun invoke(uri: Uri): List<ContactUiModel> {
        return repository.extractDataFromFile(uri).map { it.toUiModel() }
    }
}