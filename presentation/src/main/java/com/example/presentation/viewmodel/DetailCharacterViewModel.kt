package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.base.SuperHeroDomainLayerBridgeImpl
import com.example.domain.domain.FailureBo
import com.example.presentation.domain.CharacterModelVo
import com.example.presentation.domain.voToBo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCharacterViewModel @Inject constructor(
    private val superHeroDomainLayerBridge: SuperHeroDomainLayerBridgeImpl
) : ViewModel() {

    private val _characterModelVo = MutableStateFlow<CharacterModelVo?>(
        null
    )
    val characterModelVo : StateFlow<CharacterModelVo?> get() = _characterModelVo

    fun updateSuperHeroFavoriteToBridge(superHero: CharacterModelVo){
        superHeroDomainLayerBridge.saveDataSuperHeroCache(superHero.voToBo()).fold(
            ifLeft = ::handleError,
            ifRight = {
                superHero.favorite = it
                viewModelScope.launch {
                    _characterModelVo.emit(superHero)//whatever happends, we need favorite status
                }
            })
    }
    private fun handleError(failureBo: FailureBo) {
        println(failureBo.toString())
    }
}