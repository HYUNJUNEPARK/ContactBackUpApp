package com.canbe.phoneguard.domain.file

interface FileRepository {
    suspend fun exportToFile(content: String)
}