package com.example.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.base.SuperHeroDomainLayerBridgeImpl
import com.example.domain.domain.FailureBo
import com.example.presentation.base.ScreenDisplay
import com.example.presentation.domain.CharacterModelVo
import com.example.presentation.domain.listBoToVo
import com.example.presentation.domain.voToBo
import com.example.presentation.feature.SuperHeroState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SuperHeroListViewModel @Inject constructor(
    private val superHeroDomainLayerBridge: SuperHeroDomainLayerBridgeImpl
) : ViewModel() {

    val superHeroListState: MutableStateFlow<SuperHeroState> by lazy {
                    MutableStateFlow(SuperHeroState.Idle)
                }
    var screenDisplay: ScreenDisplay = ScreenDisplay.RegularList
    private var isLoading = false

    private fun handleError(failureBo: FailureBo) {
        viewModelScope.launch {
            superHeroListState.emit(SuperHeroState.ShowErrorState(failureBo.msg))
        }
    }

    fun updateSuperHeroList(superHero: CharacterModelVo, position: Int) =
        viewModelScope.launch {
            superHeroDomainLayerBridge.saveDataSuperHeroCache(superHero.voToBo()).fold(
                ifLeft = ::handleError,
                ifRight = {
                    superHero.favorite = it
                    superHeroListState.emit(
                        if (screenDisplay == ScreenDisplay.FavoriteList && !it) {
                            SuperHeroState.RemoveItem(position)
                        } else {
                            SuperHeroState.UpdateItem(position)
                        }
                    )
                }
            )
        }

    fun setListToShow(screenDisplay: ScreenDisplay, local:Boolean) {
        this.screenDisplay = screenDisplay
        when (screenDisplay) {
            is ScreenDisplay.FavoriteList -> updateListWithFavorite()
            is ScreenDisplay.RegularList -> fetchHeroList(local)
            else -> {}
        }
    }

    private fun updateListWithFavorite() {
        viewModelScope.launch {
            superHeroDomainLayerBridge.getFavoriteSuperHeroListCache().fold(::handleError) {
                if(it.isEmpty()) superHeroListState.emit(SuperHeroState.EmptyList)
                else superHeroListState.emit(SuperHeroState.ShowList(it.listBoToVo()))
            }
        }
    }

    private fun fetchHeroList(local: Boolean) {
        viewModelScope.launch {
            superHeroDomainLayerBridge.getLocalDataSuperHero(local).fold(::handleError) {
                superHeroListState.emit(SuperHeroState.ShowList(it.listBoToVo()))
            }
        }
    }

    fun onViewCreated() {
        when(screenDisplay){
            ScreenDisplay.RegularList -> {
                fetchHeroList( true)
            }
          else -> updateListWithFavorite()
        }
    }

    fun getIsLoading() = this.isLoading
    fun upDateIsLoading(){
        isLoading = !isLoading
    }
}