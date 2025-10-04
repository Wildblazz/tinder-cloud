package io.github.wildblazz.profile_service

import io.github.wildblazz.profile_service.configuration.TestConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfiguration::class)
class ProfileServiceApplicationTests {

    @Test
    fun contextLoads() {
    }
}
