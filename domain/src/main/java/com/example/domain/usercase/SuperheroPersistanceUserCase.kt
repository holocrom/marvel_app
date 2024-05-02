package com.example.domain.usercase

import arrow.core.Either
import com.example.domain.DomainLayerContract
import com.example.domain.domain.CharacterModelBo
import com.example.domain.domain.FailureBo
import javax.inject.Inject

class SuperheroPersistanceUserCase @Inject constructor(private val repository: DomainLayerContract) {

    fun getFavoriteSuperHeroList(): Either<FailureBo, List<CharacterModelBo>> =
        repository.getListOfFavoritesFromCache()

    fun saveSuperHeroInFavoriteList(superH : CharacterModelBo):Either<FailureBo, Boolean> =
        repository.changeSuperheroInCache(superH)


}