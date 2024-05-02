package com.example.data.net

import com.example.data.domain.CharacterRestModelDto
import com.example.data.domain.MarvelApiResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apikey:String,
        @Query("ts") ts:String,
        @Query("hash") hash:String,
        @Query("limit") limit: Int,//set limit of elements to show
        @Query("offset") offset: Int//Skip the specified number of resources in the result set
    ) : Response<MarvelApiResponseDto<CharacterRestModelDto>>

    @GET("/v1/public/characters")
    suspend fun getCharacter(
        @Query("name") name: String,
        @Query("apikey") apikey:String,
        @Query("hash") hash:String,
        @Query("ts") ts:String
    ) : Response<MarvelApiResponseDto<CharacterRestModelDto>>
}