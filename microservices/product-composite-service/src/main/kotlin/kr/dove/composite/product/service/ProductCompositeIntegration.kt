package kr.dove.composite.product.service

import kr.dove.api.routes.product.Product
import kr.dove.api.routes.review.Review
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductCompositeIntegration(
    builder: WebClient.Builder,
) {

    private val client: WebClient = builder.build()
    private val productServiceUri = "http://product"
    private val reviewServiceUri = "http://review"

    fun createProduct(product: Product): Mono<Product> =
        client
            .post()
            .uri("$productServiceUri/product")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(product)
            .retrieve()
            .bodyToMono(Product::class.java)

    fun getProduct(productId: Long): Mono<Product> =
        client
            .get()
            .uri("$productServiceUri/product/$productId")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Product::class.java)

    fun deleteProduct(productId: Long): Mono<Long> =
        client
            .delete()
            .uri("$productServiceUri/product/$productId")
            .retrieve()
            .bodyToMono(Long::class.java)

    fun createReview(review: Review): Mono<Review> =
        client
            .post()
            .uri("$reviewServiceUri/review")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(review)
            .retrieve()
            .bodyToMono(Review::class.java)

    fun getReviews(productId: Long): Flux<Review> =
        client
            .get()
            .uri("$reviewServiceUri/review/$productId")
            .accept(MediaType.APPLICATION_NDJSON)
            .retrieve()
            .bodyToFlux(Review::class.java)

    fun deleteReviews(productId: Long): Flux<Long> =
        client
            .delete()
            .uri("$reviewServiceUri/review/$productId")
            .retrieve()
            .bodyToFlux(Long::class.java)
}