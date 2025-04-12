package io.github.wildblazz.profile_service.service

import java.io.InputStream

interface StorageService {
    fun uploadFile(fileName: String, inputStream: InputStream, contentType: String): String
    fun deleteFile(fileName: String)
}
