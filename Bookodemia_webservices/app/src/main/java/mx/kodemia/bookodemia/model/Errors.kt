package mx.kodemia.bookodemia.model

import kotlinx.serialization.Serializable

@Serializable
data class Errors(
    val errors: List<Error>
)
