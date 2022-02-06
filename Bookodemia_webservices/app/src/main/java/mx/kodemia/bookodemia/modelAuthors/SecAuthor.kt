package mx.kodemia.bookodemia.modelAuthors

import kotlinx.serialization.Serializable

@Serializable
data class SecAuthor(
    val type: String,
    val id: String,
    val attributes: SecAttributes,
    val relationships: SecRelationships,
    val links: SecLinks
)
