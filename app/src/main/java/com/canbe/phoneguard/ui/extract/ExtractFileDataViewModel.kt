package com.canbe.phoneguard.ui.extract

import android.content.ContentProviderOperation
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.canbe.phoneguard.data.model.ContactDto
import com.canbe.phoneguard.domain.file.ExtractFileDataUseCase
import com.canbe.phoneguard.ui.base.BaseViewModel
import com.canbe.phoneguard.ui.model.ContactUiModel
import com.canbe.phoneguard.ui.model.UiState
import com.canbe.phoneguard.ui.model.toDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExtractFileDataViewModel @Inject constructor(
    private val extractFileDataUseCase: ExtractFileDataUseCase
) : BaseViewModel() {
    private val _contactList = mutableStateOf<List<ContactUiModel>>(emptyList())
    val contactList: State<List<ContactUiModel>> = _contactList

    fun extractFromFile(uri: Uri) = viewModelScope.launch(Dispatchers.IO) {
        Timber.d("extractFromFile(): $uri")
        updateUiState(UiState.Loading)

        val fileContent = extractFileDataUseCase(uri)
        Timber.d("extractFromFile() fileContent: $fileContent")

        _contactList.value = fileContent

        updateUiState(UiState.Success)
    }

    fun saveDataToDevice(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val data = contactList.value.map { it.toDto() }

        saveContactsToDevice(context, data)
    }



    private suspend fun saveContactsToDevice(context: Context, contactList: List<ContactDto>) {
        Timber.d("saveContactsToDevice()")
        withContext(Dispatchers.IO) {
            contactList.forEach { contact ->
                val operations = ArrayList<ContentProviderOperation>()

                // 1. 새로운 RawContact 추가
                operations.add(
                    ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build()
                )

                // 2. 이름 추가
                contact.displayNamePrimary?.let {
                    operations.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                            )
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, it)
                            .build()
                    )
                }

                // 3. 전화번호 추가
                contact.phoneNumbers.forEach { number ->
                    operations.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                            )
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                            .build()
                    )
                }

                // 4. 이메일 추가
                contact.emailAddresses.forEach { email ->
                    operations.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                            )
                            .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                            .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                            .build()
                    )
                }

                // 5. 조직 추가
                contact.organizationCompany?.let {
                    operations.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                            )
                            .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, it)
                            .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                            .build()
                    )
                }

                // 6. 메모 추가
                contact.notes?.let {
                    operations.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE
                            )
                            .withValue(ContactsContract.CommonDataKinds.Note.NOTE, it)
                            .build()
                    )
                }

                // 7. 사진 URI는 직접 넣을 수 없고, InputStream 으로 처리해야 하므로 생략 또는 확장 처리 필요

                try {
                    context.contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
                } catch (e: Exception) {
                    Timber.e("testLog $e")
                    e.printStackTrace()
                    // 예외 처리 (중복, 권한 없음 등)
                    //Permission Denial: writing com.samsung.android.providers.contacts.SamsungContactsProvider2 uri content://com.android.contacts/raw_contacts from pid=27442, uid=10346 requires android.permission.WRITE_CONTACTS, or grantUriPermission()

                }
            }
        }
    }

}
