package com.example.data.domain

import com.example.domain.domain.ErrorMessage
import com.example.domain.utils.Thumbnail
import com.google.gson.annotations.SerializedName


//--------------------------------------------------------------------------------
data class CharacterModelDto(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("thumbnail")
    val thumbnail : Thumbnail
    )

//--------------------------------------------------------------------------------
sealed class FailureDto(val msg : String?){
    companion object {
        private const val DEFAULT_ERROR_CODE = -1
    }

    class ConnectionError : FailureDto(msg=ErrorMessage.ERROR_NO_DATA)
}


data class MarvelApiResponseDto<T> (
    @SerializedName("code")
    val code : Int,
    @SerializedName("status")
    val status : String,
    @SerializedName("data")
    val data : MarvelApiDataDto<T>
)

data class CharacterRestModelDto(

    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("thumbnail")
    val thumbnail : Thumbnail
)

data class MarvelApiDataDto<T>(

    @SerializedName("offset")
    val offset : Int,
    @SerializedName("limit")
    val limit : Int,
    @SerializedName("total")
    val total : Int,
    @SerializedName("count")
    val count : Int,
    @SerializedName("results")
    val results : List<CharacterModelDto>
)

