package com.example.data.repository

import arrow.core.*
import com.example.data.datasource.CacheDataSource
import com.example.data.datasource.ConnectionToApiDataSource
import com.example.domain.DomainLayerContract
import com.example.domain.domain.CharacterModelBo
import com.example.domain.domain.FailureBo
import javax.inject.Inject

class Repository @Inject constructor(
    private val connectionToApiDataSource: ConnectionToApiDataSource,
    private val cacheDataSource: CacheDataSource
): DomainLayerContract {

    override fun changeSuperheroInCache(superHero:CharacterModelBo): Either<FailureBo, Boolean> =
        cacheDataSource.updateSuperHero(superHero)

    override fun getListOfFavoritesFromCache(): Either<FailureBo, ArrayList<CharacterModelBo>> =
        cacheDataSource.getListOfFavoritesFromDataSource()

    override suspend fun getSuperHeroList(local:Boolean) : Either<FailureBo, List<CharacterModelBo>> =

        if(local){
            connectionToApiDataSource.fetchPaginationListSuperHero().flatMap {
                cacheDataSource.fetchLocalListOfSuperHero(it)
                it.right()
            }
        }else cacheDataSource.getLocalListOfSuperHero().right()

    override suspend fun getASingleSuperHero(name:String) : Either<FailureBo, CharacterModelBo> =
        connectionToApiDataSource.getASingleSuperHero(name)
}
