package io.github.wildblazz.profile_service.configuration

import io.minio.MinioClient
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository

@TestConfiguration
class TestConfiguration {
    @Bean
    @Primary
    fun minioClient(): MinioClient {
        return Mockito.mock(MinioClient::class.java)
    }

    @Bean
    @Primary
    fun oauth2AuthorizedClientManager(): OAuth2AuthorizedClientManager =
        Mockito.mock(OAuth2AuthorizedClientManager::class.java)

    @Bean
    @Primary
    fun clientRegistrationRepository(): ClientRegistrationRepository =
        Mockito.mock(ClientRegistrationRepository::class.java)
}
