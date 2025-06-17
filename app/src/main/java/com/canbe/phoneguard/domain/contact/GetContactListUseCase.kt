package com.canbe.phoneguard.domain.contact

import com.canbe.phoneguard.ui.main.ContactUiModel
import javax.inject.Inject

class GetContactListUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend fun invoke(): List<ContactUiModel> {
        return repository.getContactList().map { it.toUiModel() }
    }
}