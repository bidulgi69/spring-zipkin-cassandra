package kr.dove.api.routes.product

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProductService {

    @PostMapping(
        value = ["/product"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun post(@RequestBody product: Product): Mono<Product>

    @GetMapping(
        value = ["/product/{productId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getByProductId(@PathVariable(name = "productId") productId: Long): Mono<Product>

    @DeleteMapping(
        value = ["/product/{productId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteByProductId(@PathVariable(name = "productId") productId: Long): Mono<Long>

    @GetMapping(
        value = ["/product/{offset}/{size}"],
        produces = [MediaType.APPLICATION_NDJSON_VALUE]
    )
    fun paging(
        @PathVariable(name = "offset") offset: Long,
        @PathVariable(name = "size") size: Long
    ): Flux<Product>
}