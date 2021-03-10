package mx.com.maiktmp.skilltestupx.ui.models

data class GenericResponse<T>(
    var success: Boolean = false,
    var message: String? = "",
    var data: T? = null,
    var code: Int? = 0,
)
