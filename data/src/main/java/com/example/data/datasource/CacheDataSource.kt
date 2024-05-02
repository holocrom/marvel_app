package com.example.data.datasource

import android.content.Context
import android.content.SharedPreferences
import arrow.core.Either
import arrow.core.right
import com.example.domain.domain.CharacterModelBo
import com.example.domain.domain.FailureBo
import com.example.domain.utils.CharacterListToShared
import com.example.domain.utils.ConstantsApp
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


interface CacheDataSource {
    fun getListOfFavoritesFromDataSource() : Either<FailureBo, ArrayList<CharacterModelBo>>
    fun updateSuperHero(superHero: CharacterModelBo): Either<FailureBo, Boolean>

    fun fetchLocalListOfSuperHero(list : List<CharacterModelBo>):Boolean
    fun getLocalListOfSuperHero() : List<CharacterModelBo>

}

class CacheDataSourceImpl @Inject constructor(@ApplicationContext private val context: Context)
    : CacheDataSource {

    var preferences : SharedPreferences = context.getSharedPreferences(
        ConstantsApp.FAVORITES_FILE_NAME, Context.MODE_PRIVATE)

    private val superHeroListWrapper: CharacterListToShared =
        loadCharacterModelBoListToCache() ?: CharacterListToShared(arrayListOf())

    private var superHeroLocalList: List<CharacterModelBo> = listOf()

    override fun fetchLocalListOfSuperHero(list : List<CharacterModelBo>):Boolean{
        var listSave: Boolean
        list.let {
            superHeroLocalList = list
            listSave = true
        }
        return listSave
    }

    override fun getLocalListOfSuperHero() = superHeroLocalList

    private fun saveFavoriteListToCache(): Either<FailureBo, Boolean> =
        saveCharacterModelBoList(superHeroListWrapper).right()

    override fun getListOfFavoritesFromDataSource(): Either<FailureBo, ArrayList<CharacterModelBo>> =
        superHeroListWrapper.arrayListOfCharacterModelBo.right()

    override fun updateSuperHero(superHero: CharacterModelBo): Either<FailureBo, Boolean> {
        addOrRemoveSuperFromLocalList(superHero)
        saveFavoriteListToCache()
        return superHeroListWrapper.arrayListOfCharacterModelBo.contains(superHero).right()
    }

    private fun addOrRemoveSuperFromLocalList(superHero: CharacterModelBo){
        with(superHeroListWrapper) {
            arrayListOfCharacterModelBo.firstOrNull { it.id == superHero.id }?.let {
                arrayListOfCharacterModelBo.remove(it)
            } ?: run {
                arrayListOfCharacterModelBo.add(superHero.apply { favorite = true })
                orderListBySuperHeroName()
            }
        }
    }

    private fun saveCharacterModelBoList(superHero: CharacterListToShared):Boolean{
        val jsonString = GsonBuilder().create().toJson(superHero)
        var isSaved =false
        try {
            preferences.edit().putString(ConstantsApp.FAVORITES_FILE_NAME, jsonString).apply()
            isSaved=true
        }catch (ex:Exception){
            println("Exception: $ex error al salvar la lista de favoritos")
        }
        return isSaved
    }

    private fun loadCharacterModelBoListToCache(): CharacterListToShared? {
        val value = preferences.getString(ConstantsApp.FAVORITES_FILE_NAME,null)
        return GsonBuilder().create().fromJson(value, CharacterListToShared::class.java)
    }

    private fun orderListBySuperHeroName(){
        superHeroListWrapper.arrayListOfCharacterModelBo.sortBy { it.name }
    }
}
