package com.example.presentation.domain

import com.example.domain.domain.CharacterModelBo

private const val DEFAULT_INTEGER_VALUE = 0
private const val DEFAULT_STRING_NAME = " "
private const val DEFAULT_STRING_VALUE = "Información no disponible"

fun List<CharacterModelBo>.listBoToVo() = map{it.boToVo()}

fun CharacterModelBo.boToVo() = CharacterModelVo(
    id = id ?: DEFAULT_INTEGER_VALUE,
    name = name ?: DEFAULT_STRING_NAME,
    description = description ?: DEFAULT_STRING_VALUE,
    imageURL = imageURL,
    favorite = favorite
)

fun CharacterModelVo.voToBo() = CharacterModelBo(
    id = id ?: DEFAULT_INTEGER_VALUE,
    name = name ?: DEFAULT_STRING_VALUE,
    description = description ?: DEFAULT_STRING_VALUE,
    imageURL = imageURL,
    favorite = favorite
)

fun CharacterModelVo.changeFavorite() = this.apply { favorite = !favorite }