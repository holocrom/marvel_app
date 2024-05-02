package com.example.domain.usercase

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.right
import com.example.domain.DomainLayerContract
import com.example.domain.domain.CharacterModelBo
import com.example.domain.domain.FailureBo
import javax.inject.Inject

class ListSuperHeroUserCase @Inject constructor(private val repository: DomainLayerContract) {

    suspend fun getSuperHeroList(local:Boolean): Either<FailureBo, List<CharacterModelBo>> =
        checkSuperHeroesInLists(
            repository.getSuperHeroList(local).getOrElse { arrayListOf() },
            repository.getListOfFavoritesFromCache().getOrElse { arrayListOf() }
        )

    private fun checkSuperHeroesInLists(
        superHero: List<CharacterModelBo>,
        favoriteHero: ArrayList<CharacterModelBo>
    ): Either<FailureBo, List<CharacterModelBo>> =
        superHero.map { superHero ->
            superHero.apply { favorite = favoriteHero.firstOrNull { this.id == it.id } != null }
        }.right()
}
