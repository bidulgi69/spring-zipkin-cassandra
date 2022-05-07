package kr.dove.service.product

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class ProductServiceApplication {

	@Bean
	@LoadBalanced
	fun builder(): WebClient.Builder = WebClient.builder()

}

fun main(args: Array<String>) {
	runApplication<ProductServiceApplication>(*args)
}
