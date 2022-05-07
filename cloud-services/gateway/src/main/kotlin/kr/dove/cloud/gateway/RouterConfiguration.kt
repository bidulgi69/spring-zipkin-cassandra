package kr.dove.cloud.gateway

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RouterConfiguration(
    @param:Value("\${app.eureka-server:eureka}") private val eurekaServer: String,
    @param:Value("\${app.config-server:config-server}") private val configServer: String,
) {

    @Bean
    fun routeLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route("product-composite") { r: PredicateSpec ->
                r
                    .path("/product-composite/**")
                    .uri("lb://product-composite")
            }
            .route("eureka-api") { r: PredicateSpec ->
                r
                    .path("/eureka/api/(?<segment>.*")
                    .filters { f: GatewayFilterSpec -> f.setPath("/eureka/(?<segment>.*") }
                    .uri(String.format("https://%s:8761", eurekaServer))
            }
            .route("eureka-web-start") { r: PredicateSpec ->
                r
                    .path("/eureka/web")
                    .filters { f: GatewayFilterSpec -> f.setPath("/") }
                    .uri(String.format("https://%s:8761", eurekaServer))
            }
            .route("eureka-web-other") { r: PredicateSpec ->
                r
                    .path("/eureka/**")
                    .uri(String.format("https://%s:8761", eurekaServer))
            }
            .route("config-server") { r: PredicateSpec ->
                r
                    .path("/config/**")
                    .filters { f: GatewayFilterSpec -> f.setPath("/config/(?<segment>.*") }
                    .uri(String.format("https://%s:8888", configServer))
            }
            .route("host_route_200") { r: PredicateSpec ->
                r.host("i.feel.lucky:8080")
                    .and()
                    .path("/headerrouting/**")
                    .filters { f: GatewayFilterSpec -> f.setPath("/200") }
                    .uri("https://httpstat.us")
            }
            .route("host_route_418") { r: PredicateSpec ->
                r.host("im.a.teapot:8080")
                    .and()
                    .path("/headerrouting/**")
                    .filters { f: GatewayFilterSpec -> f.setPath("/418") }
                    .uri("https://httpstat.us")
            }
            .route("host_route_501") { r: PredicateSpec ->
                r
                    .path("/headerrouting/**")
                    .filters { f: GatewayFilterSpec -> f.setPath("/501") }
                    .uri("https://httpstat.us")
            }
            .build()
    }
}