package com.example.domain

import arrow.core.Either
import com.example.domain.domain.CharacterModelBo
import com.example.domain.domain.FailureBo

interface DomainLayerContract {

    fun changeSuperheroInCache(superHero:CharacterModelBo) : Either<FailureBo, Boolean>

    fun getListOfFavoritesFromCache(): Either<FailureBo, ArrayList<CharacterModelBo>>
    //TODO une getListOfFavoritesFromCache y getLocalSuperHeroList
    //TODO no las uno porque getListOfFavoritesFromCache se usa, ppalmente para recuperar la lista y compararla con la lista completa, no para
    //mostrarla por pantalla...
    suspend fun getSuperHeroList(local:Boolean) : Either<FailureBo, List<CharacterModelBo>>

    suspend fun getASingleSuperHero(name:String) : Either<FailureBo, CharacterModelBo>//get a single superhero
}

