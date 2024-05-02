package com.example.presentation.domain

import java.io.Serializable

data class CharacterModelVo (
    val id : Int,
    val name : String?,
    val description : String?,
    val imageURL: String?,
    var favorite : Boolean
): Serializable