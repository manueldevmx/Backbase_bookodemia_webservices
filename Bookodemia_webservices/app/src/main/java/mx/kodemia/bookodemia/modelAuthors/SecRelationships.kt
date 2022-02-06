package mx.kodemia.bookodemia.modelAuthors

import kotlinx.serialization.Serializable

@Serializable
data class SecRelationships(
    val books: SecBooks
)
