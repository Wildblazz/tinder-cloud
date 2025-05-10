package io.github.wildblazz.match_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["io.github.wildblazz.shared", "io.github.wildblazz.match_service"])
class MatchServiceApplication

fun main(args: Array<String>) {
    runApplication<MatchServiceApplication>(*args)
}
