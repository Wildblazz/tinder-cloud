package io.github.wildblazz.shared.exception

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class MessageConfig {

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasenames(
            "classpath:messages",
            "classpath:shared/messages"
        )
        messageSource.setDefaultEncoding("UTF-8")

        return messageSource
    }
}
