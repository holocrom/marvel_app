package com.example.domain.base

import arrow.core.Either
import com.example.domain.domain.CharacterModelBo
import com.example.domain.domain.FailureBo
import com.example.domain.usercase.SuperheroPersistanceUserCase
import com.example.domain.usercase.ListSuperHeroUserCase
import javax.inject.Inject

interface SuperHeroDomainLayerBridge : BaseDomainLayerBridge {

    fun saveDataSuperHeroCache(superHero : CharacterModelBo) : Either<FailureBo, Boolean>
    fun getFavoriteSuperHeroListCache() : Either<FailureBo, List<CharacterModelBo>>
    suspend fun getLocalDataSuperHero(local:Boolean) : Either<FailureBo, List<CharacterModelBo>>
}

class SuperHeroDomainLayerBridgeImpl @Inject constructor(
    private val listSuperUc: ListSuperHeroUserCase,
    private val getFavoriteListCacheUc: SuperheroPersistanceUserCase
):SuperHeroDomainLayerBridge {

    override fun saveDataSuperHeroCache(superHero : CharacterModelBo) : Either<FailureBo, Boolean> =
        getFavoriteListCacheUc.saveSuperHeroInFavoriteList(superHero)

    override fun getFavoriteSuperHeroListCache(): Either<FailureBo, List<CharacterModelBo>> =
        getFavoriteListCacheUc.getFavoriteSuperHeroList()

    override suspend fun getLocalDataSuperHero(local:Boolean) : Either<FailureBo, List<CharacterModelBo>> =
        listSuperUc.getSuperHeroList(local)
}


