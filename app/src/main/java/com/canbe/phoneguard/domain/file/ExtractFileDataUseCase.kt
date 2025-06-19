package com.canbe.phoneguard.domain.file

import android.net.Uri
import com.canbe.phoneguard.domain.model.toUiModel
import com.canbe.phoneguard.ui.model.ContactUiModel
import javax.inject.Inject

class ExtractFileDataUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend operator fun invoke(uri: Uri): List<ContactUiModel> {
        return repository.extractDataFromFile(uri).map { it.toUiModel() }
    }
}