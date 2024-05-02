package com.example.data.datasource

import android.os.Build
import androidx.annotation.RequiresApi
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.data.ApiClient
import com.example.data.domain.dtoToBo
import com.example.data.domain.listDtoToBo
import com.example.data.net.ApiService
import com.example.data.utils.MarvelApiConstant
import com.example.data.utils.MarvelApiConstant.MARVEL_API_PUBLIC_KEY_SECOND
import com.example.domain.domain.CharacterModelBo
import com.example.domain.domain.FailureBo
import retrofit2.Retrofit
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDate
import javax.inject.Inject


interface ConnectionToApiDataSource {
    suspend fun fetchPaginationListSuperHero(): Either<FailureBo, List<CharacterModelBo>>//fun to get paginationList
    suspend fun getASingleSuperHero(name:String) : Either<FailureBo, CharacterModelBo>
}

class ConnectionDataSource @Inject constructor(private val retrofit: ApiClient):
    ConnectionToApiDataSource {

    private var amountOffset = 0

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun fetchPaginationListSuperHero(): Either<FailureBo, List<CharacterModelBo>> =

        try {
            val currentDate = LocalDate.now().toString()
            val call = retrofit.getRetrofit().create(ApiService::class.java).getCharacters(
                MARVEL_API_PUBLIC_KEY_SECOND,
                currentDate,
                md5(currentDate + MarvelApiConstant.MARVEL_API_PRIVATE_KEY_SECOND + MARVEL_API_PUBLIC_KEY_SECOND),
                100,amountOffset
            )
            if (call.isSuccessful){
                amountOffset += call.body()!!.data.limit
                call.body()!!.data.results.listDtoToBo().right()
            }

            else FailureBo.NoClass.left()

        } catch (ex: Exception) {
            println("Exception: $ex while fetchPaginationListSuperHeroes")
            FailureBo.NoClass.left()
        }


    override suspend fun getASingleSuperHero(name:String) : Either<FailureBo, CharacterModelBo> =

        try {
            val ts = System.currentTimeMillis().toString()
            val call = retrofit.getRetrofit().create(ApiService::class.java).getCharacter(
                name,
                MARVEL_API_PUBLIC_KEY_SECOND,
                md5(ts + MarvelApiConstant.MARVEL_API_PRIVATE_KEY_SECOND + MARVEL_API_PUBLIC_KEY_SECOND),
                ts
            )
            if (call.isSuccessful) call.body()!!.data.results.first().dtoToBo().right()//en teoria, el primero de los que correspondan...
            else FailureBo.NoClass.left()

        } catch (ex: Exception) {
            println("Exception: $ex while fetchPaginationListSuperHeroes")
            FailureBo.NoClass.left()
        }
}

