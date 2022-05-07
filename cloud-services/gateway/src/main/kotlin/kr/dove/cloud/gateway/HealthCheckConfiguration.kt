package kr.dove.cloud.gateway

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.ReactiveHealthContributor
import org.springframework.boot.actuate.health.ReactiveHealthIndicator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.logging.Level

@Configuration
class HealthCheckConfiguration(
    builder: WebClient.Builder
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val client: WebClient = builder.build()

    @Bean
    fun healthCheckServices(): ReactiveHealthContributor {
        val registry: MutableMap<String, ReactiveHealthIndicator> = LinkedHashMap()

        registry["product-composite"] = ReactiveHealthIndicator { getHealth("http://product-composite") }
        registry["product"] = ReactiveHealthIndicator { getHealth("http://product") }
        registry["review"] = ReactiveHealthIndicator { getHealth("http://review") }

        return CompositeReactiveHealthContributor.fromMap(registry)
    }

    private fun getHealth(baseUrl: String): Mono<Health> {
        val url = "$baseUrl/actuator/health"
        logger.debug("Will call to the actuator api on URL: {}", url)
        return client
            .get()
            .uri(url)
            .retrieve()
            .bodyToMono(String::class.java)
            .flatMap {
                Mono.just(
                    Health.Builder()
                        .up()
                        .build()
                )
            }
            .onErrorResume { ex ->
                Mono.just(
                    Health.Builder()
                        .down(ex)
                        .build()
                )
            }
            .log(logger.name, Level.FINE)
    }
}