package com.canbe.contactbackup.data.file

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import com.canbe.contactbackup.data.model.ContactDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import javax.inject.Inject

class FileDataSource @Inject constructor(
    private val contentResolver: ContentResolver
) {
    /**
     * JSON 파일 저장
     */
    fun exportToFile(
        fileName: String,
        fileContent: String,
        directory: String = Environment.DIRECTORY_DOWNLOADS
    ) {
        Timber.i("exportToFile() $fileContent")

        /*
            API 29 이상 : MediaStore 를 사용하여 Download 폴더에 접근
            API 29 미만 : 직접 Download 폴더 경로에 파일 생성
         */
        val outputStream: OutputStream? = if (Build.VERSION.SDK_INT >= 29) {
            //API 29 이상
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/json")
                put(MediaStore.Downloads.RELATIVE_PATH, directory) // 저장 경로 (Downloads)
                put(MediaStore.Downloads.IS_PENDING, 1)  // 저장 중 상태 표시
            }

            val resolver = contentResolver
            val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val itemUri: Uri? = resolver.insert(collection, contentValues)

            // outputStream 을 열고, 저장 완료 후 IS_PENDING 을 0으로 변경
            itemUri?.let { uri ->
                resolver.openOutputStream(uri)?.also {
                    contentValues.clear()
                    contentValues.put(MediaStore.Downloads.IS_PENDING, 0) //저장 완료 상태 표시 -> 0 으로 바꿔줘야 다른 앱에서 접근이 가능
                    resolver.update(uri, contentValues, null, null)
                }
            }
        } else {
            //API 29 미만
            val downloadsDir = Environment.getExternalStoragePublicDirectory(directory)
            val file = File(downloadsDir, fileName)
            FileOutputStream(file)
        }

        try {
            // OutputStream 에 JSON 문자열을 바이트 배열로 변환하여 저장
            // 해당 코드를 실행시키지 않는다면, 실제 파일에는 아무런 데이터도 저장되지 않는다.
            outputStream?.use {
                it.write(fileContent.toByteArray())
            }
        } catch (e: IOException) {
            Timber.e("Exception exportToFile(): $e")
            e.printStackTrace()
            throw e
        }
    }

    /**
     * 선택한 JSON 파일에서 데이터 추출
     */
    fun extractDataFromFile(uri: Uri): List<ContactDto> {
        try {
            Timber.i("extractDataFromFile() $uri")

            val resolver = contentResolver

            val fileStrData = resolver.openInputStream(uri)?.use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    reader.readText()
                }
            }

            if (fileStrData == null) throw NullPointerException("Read File Result is null")
            Timber.d("extractDataFromFile() fileContent(JsonStr): $fileStrData")

            val type = object : TypeToken<List<ContactDto>>() {}.type
            val contactList: List<ContactDto> = Gson().fromJson(fileStrData, type)
            Timber.d("extractDataFromFile() contactList(Dto): $contactList")

            return contactList
        } catch (e: Exception) {
            Timber.e("Exception extractDataFromFile(): $e")
            e.printStackTrace()
            throw e
        }
    }

    /**
     * cf. 이름, 전화번호가 중복되는 경우 저장되지 않음
     */
    fun saveContactsToDevice(contactList: List<ContactDto>) {
        Timber.i("saveContactsToDevice() contactList size(${contactList.size})")

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
                Timber.i("saveContactsToDevice() operations: $operations")
                contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
            } catch (e: Exception) {
                Timber.e("Exception saveContactsToDevice(): $e")
                e.printStackTrace()
                throw e
            }
        }
    }
}