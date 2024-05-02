package com.example.domain.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.domain.domain.CharacterModelBo
import com.example.domain.utils.ConstantsApp.FAVORITES_FILE_NAME
import com.google.gson.GsonBuilder

object SharedPreferencesManager {

    lateinit var preferences: SharedPreferences

    fun with(application: Application) {
        preferences = application.getSharedPreferences(
            FAVORITES_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun saveCharacterModelBoList(superHero: CharacterListToShared):Boolean{
        val jsonString = GsonBuilder().create().toJson(superHero)
        var superHeroSaved=false
        try {
            preferences.edit().putString(FAVORITES_FILE_NAME, jsonString).apply()
            superHeroSaved=true
        }catch (e:Exception){}
        return superHeroSaved
    }

    inline fun <reified T> loadCharacterModelBoListToPersistance() : T? {
        val value = preferences.getString(FAVORITES_FILE_NAME,null)
        return GsonBuilder().create().fromJson(value, T::class.java)

    }
    fun loadDos(): ArrayList<CharacterModelBo>{
        val value = preferences.getString(FAVORITES_FILE_NAME,null)
        return GsonBuilder().create().fromJson(value, CharacterListToShared::class.java).arrayListOfCharacterModelBo
    }
    //----------------------------------------------------------------------------
    inline fun <reified T> getSuperHeroFavoriteListFromPersistance(key: String): T? {
        //We read JSON String which was saved.
        val value = preferences.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
    //----------------------------------------------------------------------------

}
