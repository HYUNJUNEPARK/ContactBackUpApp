package com.canbe.contactbackup.domain.contact

import com.canbe.contactbackup.domain.model.toUiModel
import com.canbe.contactbackup.ui.model.ContactUiModel
import javax.inject.Inject

class GetContactListUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend fun invoke(): List<ContactUiModel> {
        return repository.getContactList().map { it.toUiModel() }
    }
}