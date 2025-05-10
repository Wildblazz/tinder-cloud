package io.github.wildblazz.recommendation_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["io.github.wildblazz.shared", "io.github.wildblazz.recommendation_service"])
class RecommendationServiceApplication

fun main(args: Array<String>) {
	runApplication<RecommendationServiceApplication>(*args)
}
