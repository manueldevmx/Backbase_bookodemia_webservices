package mx.kodemia.bookodemia.modelAuthors

import kotlinx.serialization.Serializable

@Serializable
data class FirstAuthor(
    val type: String,
    val id: String
)
