package io.github.wildblazz.shared.event.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("kafka")
class KafkaProperties {
    var defaultPartitions: Int = 3
    var partitions: Map<String, Int> = emptyMap()
}
