package mx.kodemia.bookodemia.modelBooks

import kotlinx.serialization.Serializable

@Serializable
data class datosLibro(
    val type: String,
    val id: String,
    val attributes: Attributes,
    val relationships: Relationships,
    val links: Links
): java.io.Serializable
