package com.example.domain.domain

private const val DEFAULT_STRING_RESOURCE = "none"

data class CharacterModelBo (
    val id : Int,
    val name : String,
    var description : String?,
    val imageURL: String?,
    var favorite : Boolean
)

sealed class FailureBo(var msg : String= DEFAULT_STRING_RESOURCE){
    object NoClass : FailureBo(msg = ErrorMessage.ERROR_NO_CONNECTION)
}

object ErrorMessage {
    const val ERROR_NO_CONNECTION = "Sin conexion"
    const val ERROR_NO_DATA = "No Data"
}