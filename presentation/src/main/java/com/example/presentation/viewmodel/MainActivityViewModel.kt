package com.example.presentation.viewmodel

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.example.domain.base.SuperHeroDomainLayerBridgeImpl
import com.example.domain.domain.FailureBo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val superHeroDomainLayerBridge: SuperHeroDomainLayerBridgeImpl
    ): ViewModel() {

    private var actualDisplay = true

    fun updateDisplays(){
        actualDisplay = !actualDisplay
    }
    fun getActualDisplay() = this.actualDisplay

}