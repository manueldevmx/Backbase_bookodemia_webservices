package mx.kodemia.bookodemia.modelBooks

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val data: MutableList<datosLibro>
): java.io.Serializable

