package com.example.presentation.ui

import com.example.presentation.base.ScreenDisplay
import com.example.presentation.domain.CharacterModelVo

interface CommunicatorBetweenFragments {

    fun passCharacterVo(superHero : CharacterModelVo)
    fun showList(screenDisplay: ScreenDisplay, local:Boolean)
    fun getActualDisplay():Boolean//to get actualDisplay state
}