package io.github.wildblazz.profile_service.service

import io.minio.*
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStream


@Service
class MinioStorageServiceImpl(
    private val minioClient: MinioClient,
    @Value("\${minio.bucket-name}") private val bucketName: String,
    @Value("\${minio.endpoint}") private val endpoint: String
) : StorageService {

    @PostConstruct
    fun init() {
        val bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
        }
    }

    override fun uploadFile(fileName: String, inputStream: InputStream, contentType: String): String {
        minioClient.putObject(
            PutObjectArgs.builder().bucket(bucketName).`object`(fileName)
                .stream(inputStream, inputStream.available().toLong(), -1).contentType(contentType).build()
        )

        return "$endpoint/$bucketName/$fileName"
    }

    override fun deleteFile(fileName: String) {
        minioClient.removeObject(
            RemoveObjectArgs.builder().bucket(bucketName).`object`(fileName).build()
        )
    }
}
