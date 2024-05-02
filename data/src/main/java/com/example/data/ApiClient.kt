package com.example.data

import com.example.data.utils.MarvelApiConstant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

interface ApiClient {
    fun getRetrofit(): Retrofit
}
class ImpleApiClient @Inject constructor ():ApiClient{
    override fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(MarvelApiConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient().newBuilder().build())
            .build()
    }
}