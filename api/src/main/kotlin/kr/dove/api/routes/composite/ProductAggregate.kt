package kr.dove.api.routes.composite

import kr.dove.api.routes.product.Product
import kr.dove.api.routes.review.Review

data class ProductAggregate(
    val product: Product,
    val reviews: List<Review>
)