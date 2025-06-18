package com.canbe.phoneguard.data.file

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.canbe.phoneguard.data.model.ContactDto
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
        fileName: String = "PHONE_NUMBER_BACKUP_${System.currentTimeMillis()}.json",
        directory: String = Environment.DIRECTORY_DOWNLOADS,
        fileContent: String
    ) {
        Timber.d("saveToDownloads() $fileContent")

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
            e.printStackTrace()
        }
    }

    /**
     * 선택한 JSON 파일에서 데이터 추출
     */
    fun extractDataFromFile(uri: Uri): List<ContactDto> {
        try {
            Timber.d("extractDataFromFile() $uri")

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
            e.printStackTrace()
            throw e
        }
    }
}