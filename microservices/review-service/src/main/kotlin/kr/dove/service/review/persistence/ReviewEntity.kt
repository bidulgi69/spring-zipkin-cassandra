package kr.dove.service.review.persistence

import kr.dove.api.routes.review.Review
import org.springframework.data.annotation.*
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document(collection = "reviews")
@CompoundIndex(
    name = "prod-rev-id",
    unique = true,
    def = "{'productId': 1, 'reviewId': 1}"
)
data class ReviewEntity(
    @Id val id: UUID,
    val productId: Long,
    val reviewId: Long,
    var author: String,
    var subject: String,
    var content: String = "",
    @Version var version: Int = 0,
) {
    @CreatedDate lateinit var created: LocalDateTime
        private set
    @LastModifiedDate lateinit var modified: LocalDateTime
        private set

    fun mapToApi(): Review =
        Review(
            productId,
            reviewId,
            author,
            subject,
            content,
            version
        )
}
