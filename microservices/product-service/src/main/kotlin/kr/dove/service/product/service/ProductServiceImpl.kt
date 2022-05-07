package kr.dove.service.product.service

import kr.dove.api.routes.product.Product
import kr.dove.api.routes.product.ProductService
import kr.dove.service.product.persistence.ProductEntity
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
class ProductServiceImpl(
    private val template: ReactiveMongoTemplate,
) : ProductService {

    override fun post(product: Product): Mono<Product> {
        return template
            .insert(ProductEntity(
                UUID.randomUUID(),
                product.name,
                product.productId,
                product.cost
            ))
            .flatMap { en ->
                Mono.just(en.mapToApi())
            }
    }

    override fun getByProductId(productId: Long): Mono<Product> {
        return template
            .findOne(
                Query.query(
                    Criteria.where("productId").`is`(productId)
                ),
                ProductEntity::class.java
            )
            .flatMap { en ->
                Mono.just(en.mapToApi())
            }
    }

    override fun deleteByProductId(productId: Long): Mono<Long> {
        return template
            .findAndRemove(
                Query.query(
                    Criteria.where("productId").`is`(productId)
                ),
                ProductEntity::class.java
            )
            .flatMap {
                Mono.just(it.productId)
            }
    }

    override fun paging(offset: Long, size: Long): Flux<Product> {
        return template
            .findAll(ProductEntity::class.java)
            .skip(offset * size)
            .take(size)
            .flatMap { en ->
                Mono.just(en.mapToApi())
            }
    }
}