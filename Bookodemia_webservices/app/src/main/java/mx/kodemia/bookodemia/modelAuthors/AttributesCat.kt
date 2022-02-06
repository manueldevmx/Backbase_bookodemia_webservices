package mx.kodemia.bookodemia.modelAuthors

import kotlinx.serialization.Serializable

@Serializable
data class AttributesCat(
    val name: String,
    val slug: String
)
