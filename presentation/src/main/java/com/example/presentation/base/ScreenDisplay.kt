package com.example.presentation.base

import com.example.presentation.domain.CharacterModelVo

sealed class ScreenDisplay:BaseScreen(){
    object FavoriteList: ScreenDisplay()
    object RegularList: ScreenDisplay()
    class Detail(superHero: CharacterModelVo): ScreenDisplay()
}
