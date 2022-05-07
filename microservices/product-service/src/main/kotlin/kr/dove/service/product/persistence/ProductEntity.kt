package kr.dove.service.product.persistence

import kr.dove.api.routes.product.Product
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "products")
data class ProductEntity(
    @Id val id: UUID,
    val name: String,
    @Indexed(unique = true) val productId: Long,
    val cost: Int,
    @Version val version: Int = 0
) {
    @CreatedDate lateinit var created: LocalDateTime
        private set
    @LastModifiedDate lateinit var modified: LocalDateTime
        private set

    fun mapToApi(): Product = Product(
        name,
        productId,
        cost,
        version
    )
}
