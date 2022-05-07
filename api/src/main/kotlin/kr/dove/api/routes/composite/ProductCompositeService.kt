package kr.dove.api.routes.composite

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Mono

interface ProductCompositeService {

    @PostMapping(
        value = ["/product-composite"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun post(@RequestBody aggregate: ProductAggregate): Mono<ProductAggregate>

    @DeleteMapping(
        value = ["/product-composite/{productId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteByProductId(@PathVariable(name = "productId") productId: Long): Mono<Void>

    @GetMapping(
        value = ["/product-composite/{productId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getByProductId(@PathVariable(name = "productId") productId: Long): Mono<ProductAggregate>
}