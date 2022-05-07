package kr.dove.api.routes.review

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReviewService {

    @PostMapping(
        value = ["/review"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun post(@RequestBody review: Review): Mono<Review>

    @GetMapping(
        value = ["/review/{productId}"],
        produces = [MediaType.APPLICATION_NDJSON_VALUE]
    )
    fun getByProductId(@PathVariable(name = "productId") productId: Long): Flux<Review>

    @DeleteMapping(
        value = ["/review/{productId}"],
        produces = [MediaType.APPLICATION_NDJSON_VALUE]
    )
    fun deleteByProductId(@PathVariable(name = "productId") productId: Long): Flux<Long>
}