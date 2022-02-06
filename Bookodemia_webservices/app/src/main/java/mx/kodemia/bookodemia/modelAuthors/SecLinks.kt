package mx.kodemia.bookodemia.modelAuthors

import kotlinx.serialization.Serializable

@Serializable
data class SecLinks(
    val self: String,
    val related: String = ""
)
