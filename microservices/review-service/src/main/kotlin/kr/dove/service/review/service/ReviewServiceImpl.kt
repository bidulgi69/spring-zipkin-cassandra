package kr.dove.service.review.service

import kr.dove.api.exceptions.InvalidInputException
import kr.dove.api.routes.review.Review
import kr.dove.api.routes.review.ReviewService
import kr.dove.service.review.persistence.ReviewEntity
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
class ReviewServiceImpl(
    private val template: ReactiveMongoTemplate,
) : ReviewService {

    override fun post(review: Review): Mono<Review> {
        return review.productId ?. let { productId ->
            template
                .insert(ReviewEntity(
                    UUID.randomUUID(),
                    productId,
                    review.reviewId,
                    review.author,
                    review.subject,
                    review.content
                ))
                .flatMap { en ->
                    Mono.just(en.mapToApi())
                }
        } ?: run { Mono.error(InvalidInputException("Invalid review (productId is not exist.)")) }
    }

    override fun getByProductId(productId: Long): Flux<Review> {
        return template
            .find(
                Query.query(
                    Criteria.where("productId").`is`(productId)
                ),
                ReviewEntity::class.java
            )
            .flatMap { en ->
                Mono.just(en.mapToApi())
            }
    }

    override fun deleteByProductId(productId: Long): Flux<Long> {
        return template
            .findAllAndRemove(
                Query.query(
                    Criteria.where("productId").`is`(productId)
                ),
                ReviewEntity::class.java
            )
            .flatMap { en ->
                Mono.just(en.reviewId)
            }
    }
}