package io.github.wildblazz.reaction_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@ComponentScan(basePackages = ["io.github.wildblazz.shared", "io.github.wildblazz.reaction_service"])
@SpringBootApplication
class ReactionServiceApplication

fun main(args: Array<String>) {
	runApplication<ReactionServiceApplication>(*args)
}
