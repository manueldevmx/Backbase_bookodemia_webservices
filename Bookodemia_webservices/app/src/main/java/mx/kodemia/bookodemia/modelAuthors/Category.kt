package mx.kodemia.bookodemia.modelAuthors

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val type: String,
    val id: String,
    val attributes: AttributesCat,
    val relationships: SecRelationships,
    val links: SecLinks
)
