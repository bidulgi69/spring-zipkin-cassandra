package kr.dove.composite.product.service

import kr.dove.api.routes.composite.ProductAggregate
import kr.dove.api.routes.composite.ProductCompositeService
import kr.dove.api.routes.product.Product
import kr.dove.api.routes.review.Review
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

@RestController
class ProductCompositeServiceImpl(
    val integration: ProductCompositeIntegration,
) : ProductCompositeService {

    override fun post(aggregate: ProductAggregate): Mono<ProductAggregate> {
        val product: Product = aggregate.product
        val reviews: List<Review> = aggregate.reviews
            .map { rev ->
                rev.apply {
                    this.productId = product.productId
                }
            }

        return Mono.zip(
            integration
                .createProduct(product),
            Flux.fromIterable(reviews)
                .flatMap { integration.createReview(it) }
                .collectList()
        ).flatMap {
            val (p, r) = it
            Mono.just(
                ProductAggregate(
                    p,
                    r
                )
            )
        }
    }

    override fun deleteByProductId(productId: Long): Mono<Void> {
        return Mono.zip(
            integration.deleteProduct(productId),
            integration.deleteReviews(productId).collectList()
        ).then()
    }

    override fun getByProductId(productId: Long): Mono<ProductAggregate> {
        val product: Mono<Product> = integration.getProduct(productId)
        val reviews: Flux<Review> = integration.getReviews(productId)

        return Mono.zip(
            product,
            reviews.collectList()
        ).flatMap {
            val (p, r) = it
            Mono.just(
                ProductAggregate(
                    p,
                    r
                )
            )
        }
    }
}