package mx.kodemia.bookodemia.modelAuthors

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SecAttributes(
    val name: String,
    @SerialName("created-at")
    val createdat: String,
    @SerialName("updated-at")
    val updatedat: String
)
