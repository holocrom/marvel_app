package com.example.presentation.feature

import com.example.presentation.base.BaseState
import com.example.presentation.domain.CharacterModelVo

sealed class SuperHeroState:BaseState() {

    object Idle : SuperHeroState()
    class ShowList(val list: List<CharacterModelVo>):SuperHeroState()
    class UpdateItem(val position: Int): SuperHeroState()
    class RemoveItem(val index: Int): SuperHeroState()
    class ShowErrorState(val message: String):SuperHeroState()
    object EmptyList : SuperHeroState()
    //TODO loading missing
}